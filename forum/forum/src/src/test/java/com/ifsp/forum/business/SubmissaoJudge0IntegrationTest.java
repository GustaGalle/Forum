package com.ifsp.forum.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifsp.forum.infrastructure.dto.SubmissaoDTO;
import com.ifsp.forum.infrastructure.dto.SubmissaoResponseDTO;
import com.ifsp.forum.infrastructure.entidys.Algoritmo;
import com.ifsp.forum.infrastructure.entidys.TipoUsuario;
import com.ifsp.forum.infrastructure.entidys.Usuario;
import com.ifsp.forum.infrastructure.repository.AlgoritmoRepository;
import com.ifsp.forum.infrastructure.repository.SubmissaoRepository;
import com.ifsp.forum.infrastructure.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

/**
 * Teste de integração que avalia submissões usando MOCK do Judge0
 *
 * Este teste usa MockRestServiceServer para simular respostas do Judge0,
 * evitando dependência de internet e limites de API.
 *
 * Testa:
 * 1. Uma submissão com código CORRETO
 * 2. Uma submissão com código ERRADO
 * 3. Uma submissão com erro de compilação
 * 4. Uma submissão com resultado incorreto
 */
@SpringBootTest
@Transactional
class SubmissaoJudge0IntegrationTest {

    @Autowired
    private SubmissaoService submissaoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AlgoritmoRepository algoritmoRepository;

    @Autowired
    private SubmissaoRepository submissaoRepository;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private Usuario usuario;
    private Algoritmo algoritmo;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        objectMapper = new ObjectMapper();

        // Configurar MockRestServiceServer para simular respostas do Judge0
        // Resetar o mock server antes de cada teste para evitar interferência entre testes
        if (mockServer != null) {
            mockServer.reset();
        }
        mockServer = MockRestServiceServer.createServer(restTemplate);

        // Criar usuário de teste
        usuario = Usuario.builder()
                .nome("Aluno Teste")
                .email("aluno.teste@example.com")
                .password("senha123")
                .tipo(TipoUsuario.ALUNO)
                .build();
        usuario = usuarioRepository.save(usuario);

        // Criar algoritmo sério: "Soma de Dois Números"
        // O problema: ler dois números inteiros e imprimir a soma
        String casosDeTeste = criarCasosDeTeste();

        // Criar algoritmo usando o formato exato do exemplo fornecido
        algoritmo = Algoritmo.builder()
                .titulo("Soma de Dois Números")
                .descricao("Some dois números")
                .linguagemPermitida("java")
                .judge0LanguageId(62) // Java (OpenJDK 13.0.1)
                .casosDeTeste(casosDeTeste)
                .gabarito("import java.util.Scanner; public class Main { public static void main(String[] args) { Scanner s = new Scanner(System.in); int a = s.nextInt(); int b = s.nextInt(); System.out.println(a + b); } }")
                .build();
        algoritmo = algoritmoRepository.save(algoritmo);
    }

    /**
     * Cria casos de teste para o algoritmo de soma
     * Usa o mesmo formato do exemplo fornecido
     */
    private String criarCasosDeTeste() throws Exception {
        // Usando o formato exato do exemplo: [{"entrada":"5\n10","saidaEsperada":"15"}]
        List<Map<String, String>> casos = new ArrayList<>();

        // Caso 1: números positivos (exemplo fornecido)
        Map<String, String> caso1 = new HashMap<>();
        caso1.put("entrada", "5\n10");
        caso1.put("saidaEsperada", "15");
        casos.add(caso1);

        // Caso 2: números negativos
        Map<String, String> caso2 = new HashMap<>();
        caso2.put("entrada", "-3\n-7");
        caso2.put("saidaEsperada", "-10");
        casos.add(caso2);

        // Caso 3: número positivo e negativo
        Map<String, String> caso3 = new HashMap<>();
        caso3.put("entrada", "10\n-5");
        caso3.put("saidaEsperada", "5");
        casos.add(caso3);

        // Caso 4: números grandes
        Map<String, String> caso4 = new HashMap<>();
        caso4.put("entrada", "1000\n2000");
        caso4.put("saidaEsperada", "3000");
        casos.add(caso4);

        return objectMapper.writeValueAsString(casos);
    }

    /**
     * Teste com código CORRETO
     * O código lê dois números e imprime a soma corretamente
     * Usa o código exato do exemplo fornecido
     */
    @Test
    void testSubmissaoComCodigoCorreto() throws Exception {
        // Código Java correto do exemplo fornecido
        String codigoCorreto = "import java.util.Scanner; public class Main { public static void main(String[] args) { Scanner s = new Scanner(System.in); int a = s.nextInt(); int b = s.nextInt(); System.out.println(a + b); } }";

        // Mockar respostas do Judge0 para todos os casos de teste (todos aprovados)
        mockarRespostaJudge0Sucesso("5\n10", "15");      // Caso 1
        mockarRespostaJudge0Sucesso("-3\n-7", "-10");    // Caso 2
        mockarRespostaJudge0Sucesso("10\n-5", "5");      // Caso 3
        mockarRespostaJudge0Sucesso("1000\n2000", "3000"); // Caso 4

        // Criar submissão
        SubmissaoDTO dto = new SubmissaoDTO();
        dto.setUsuarioId(usuario.getId());
        dto.setAlgoritmoId(algoritmo.getId());
        dto.setCodigo(codigoCorreto);

        // Criar submissão - a avaliação no Judge0 é automática
        SubmissaoResponseDTO resultado = submissaoService.criarSubmissao(dto);

        // Verificações - a submissão já vem avaliada pelo Judge0
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertNotNull(resultado.getAprovado(), "O campo 'aprovado' deve ser definido pelo Judge0");
        assertTrue(resultado.getAprovado(),
                   "A submissão com código correto deveria ser aprovada. Feedback: " + resultado.getFeedback());
        assertNotNull(resultado.getFeedback());
        assertTrue(resultado.getFeedback().contains("Aprovado") ||
                   resultado.getFeedback().contains("Status: Aprovado"),
                   "O feedback deveria indicar aprovação. Feedback: " + resultado.getFeedback());

        // Verificar que foi salvo no banco
        var submissaoSalva = submissaoRepository.findById(resultado.getId());
        assertTrue(submissaoSalva.isPresent());
        assertTrue(submissaoSalva.get().getAprovado(), "O campo 'aprovado' deve ser definido apenas pelo Judge0");

        // Verificar que todas as requisições mockadas foram chamadas
        mockServer.verify();
    }

    /**
     * Teste com código ERRADO
     * O código tem um erro: subtrai ao invés de somar
     */
    @Test
    void testSubmissaoComCodigoErrado() throws Exception {
        // Código Java ERRADO: subtrai ao invés de somar (baseado no exemplo correto)
        String codigoErrado = "import java.util.Scanner; public class Main { public static void main(String[] args) { Scanner s = new Scanner(System.in); int a = s.nextInt(); int b = s.nextInt(); System.out.println(a - b); } }";

        // Mockar respostas do Judge0 - código executa mas produz saída errada (subtrai ao invés de somar)
        mockarRespostaJudge0Sucesso("5\n10", "-5");       // Caso 1: esperado 15, obtido -5
        mockarRespostaJudge0Sucesso("-3\n-7", "4");       // Caso 2: esperado -10, obtido 4
        mockarRespostaJudge0Sucesso("10\n-5", "15");      // Caso 3: esperado 5, obtido 15
        mockarRespostaJudge0Sucesso("1000\n2000", "-1000"); // Caso 4: esperado 3000, obtido -1000

        // Criar submissão
        SubmissaoDTO dto = new SubmissaoDTO();
        dto.setUsuarioId(usuario.getId());
        dto.setAlgoritmoId(algoritmo.getId());
        dto.setCodigo(codigoErrado);

        // Criar submissão - a avaliação no Judge0 é automática
        SubmissaoResponseDTO resultado = submissaoService.criarSubmissao(dto);

        // Verificações - a submissão já vem avaliada pelo Judge0
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertNotNull(resultado.getAprovado(), "O campo 'aprovado' deve ser definido pelo Judge0");
        assertFalse(resultado.getAprovado(),
                    "A submissão com código errado deveria ser reprovada. Feedback: " + resultado.getFeedback());
        assertNotNull(resultado.getFeedback());
        assertTrue(resultado.getFeedback().contains("Falhou") ||
                   resultado.getFeedback().contains("Status: Falhou"),
                   "O feedback deveria indicar falha. Feedback: " + resultado.getFeedback());

        // Verificar que foi salvo no banco
        var submissaoSalva = submissaoRepository.findById(resultado.getId());
        assertTrue(submissaoSalva.isPresent());
        assertFalse(submissaoSalva.get().getAprovado(), "O campo 'aprovado' deve ser definido apenas pelo Judge0");

        // Verificar que todas as requisições mockadas foram chamadas
        mockServer.verify();
    }

    /**
     * Teste com código que tem ERRO DE COMPILAÇÃO
     * O código tem sintaxe incorreta
     */
    @Test
    void testSubmissaoComErroDeCompilacao() throws Exception {
        // Código Java com erro de sintaxe
        String codigoComErro = "import java.util.Scanner;\n" +
                              "public class Main {\n" +
                              "    public static void main(String[] args) {\n" +
                              "        Scanner scanner = new Scanner(System.in);\n" +
                              "        int a = scanner.nextInt();\n" +
                              "        int b = scanner.nextInt();\n" +
                              "        System.out.println(a + b // ERRO: falta fechar parênteses\n" +
                              "    }\n" +
                              "}";

        // Mockar respostas do Judge0 com erro de compilação (status 6) para todos os casos
        // O serviço processa todos os casos, então precisamos mockar todos
        mockarRespostaJudge0ErroCompilacao("5\n10");      // Caso 1
        mockarRespostaJudge0ErroCompilacao("-3\n-7");    // Caso 2
        mockarRespostaJudge0ErroCompilacao("10\n-5");    // Caso 3
        mockarRespostaJudge0ErroCompilacao("1000\n2000"); // Caso 4

        // Criar submissão
        SubmissaoDTO dto = new SubmissaoDTO();
        dto.setUsuarioId(usuario.getId());
        dto.setAlgoritmoId(algoritmo.getId());
        dto.setCodigo(codigoComErro);

        // Criar submissão - a avaliação no Judge0 é automática
        // O código com erro de compilação será detectado pelo Judge0
        SubmissaoResponseDTO resultado = submissaoService.criarSubmissao(dto);

        // Verificações - o Judge0 deve detectar o erro de compilação
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertNotNull(resultado.getAprovado(), "O campo 'aprovado' deve ser definido pelo Judge0");
        assertFalse(resultado.getAprovado(),
                    "Código com erro de compilação deveria ser reprovado pelo Judge0");
        assertNotNull(resultado.getFeedback());
        assertTrue(resultado.getFeedback().contains("Falhou") ||
                   resultado.getFeedback().contains("Erro") ||
                   resultado.getFeedback().contains("Compilação"),
                   "O feedback deveria indicar erro de compilação. Feedback: " + resultado.getFeedback());

        // Verificar que a requisição mockada foi chamada
        mockServer.verify();
    }

    /**
     * Teste com código que imprime resultado INCORRETO
     * O código compila e executa, mas produz saída errada
     */
    @Test
    void testSubmissaoComResultadoIncorreto() throws Exception {
        // Código que compila mas produz resultado errado (multiplica ao invés de somar)
        String codigoIncorreto = "import java.util.Scanner;\n" +
                                 "public class Main {\n" +
                                 "    public static void main(String[] args) {\n" +
                                 "        Scanner scanner = new Scanner(System.in);\n" +
                                 "        int a = scanner.nextInt();\n" +
                                 "        int b = scanner.nextInt();\n" +
                                 "        System.out.println(a * b); // ERRO: multiplica ao invés de somar\n" +
                                 "    }\n" +
                                 "}";

        // Mockar respostas do Judge0 - código executa mas produz saída errada (multiplica ao invés de somar)
        mockarRespostaJudge0Sucesso("5\n10", "50");        // Caso 1: esperado 15, obtido 50
        mockarRespostaJudge0Sucesso("-3\n-7", "21");       // Caso 2: esperado -10, obtido 21
        mockarRespostaJudge0Sucesso("10\n-5", "-50");     // Caso 3: esperado 5, obtido -50
        mockarRespostaJudge0Sucesso("1000\n2000", "2000000"); // Caso 4: esperado 3000, obtido 2000000

        // Criar submissão
        SubmissaoDTO dto = new SubmissaoDTO();
        dto.setUsuarioId(usuario.getId());
        dto.setAlgoritmoId(algoritmo.getId());
        dto.setCodigo(codigoIncorreto);

        // Criar submissão - a avaliação no Judge0 é automática
        SubmissaoResponseDTO resultado = submissaoService.criarSubmissao(dto);

        // Verificações - o Judge0 deve detectar que o resultado está incorreto
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertNotNull(resultado.getAprovado(), "O campo 'aprovado' deve ser definido pelo Judge0");
        assertFalse(resultado.getAprovado(),
                    "Código que produz resultado incorreto deveria ser reprovado pelo Judge0");
        assertNotNull(resultado.getFeedback());
        assertTrue(resultado.getFeedback().contains("Falhou") ||
                   resultado.getFeedback().contains("Status: Falhou") ||
                   resultado.getFeedback().contains("Saída esperada") ||
                   resultado.getFeedback().contains("Saída do código"),
                   "Feedback deveria mostrar diferença entre saída esperada e obtida: " + resultado.getFeedback());

        // Verificar que todas as requisições mockadas foram chamadas
        mockServer.verify();
    }

    /**
     * Helper method para mockar resposta de sucesso do Judge0
     */
    private void mockarRespostaJudge0Sucesso(String entrada, String saida) throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 3); // Status 3 = Accepted
        response.put("stdout", saida);
        response.put("compile_output", null);
        response.put("stderr", null);

        String responseJson = objectMapper.writeValueAsString(response);

        mockServer.expect(requestTo("https://judge0-ce.p.rapidapi.com/submissions?base64_encoded=false&wait=true"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("X-RapidAPI-Key", "a0901aebcbmsh9f8745681497560p1c9e05jsn6a7d905366c"))
                .andExpect(header("X-RapidAPI-Host", "judge0-ce.p.rapidapi.com"))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));
    }

    /**
     * Helper method para mockar resposta de erro de compilação do Judge0
     */
    private void mockarRespostaJudge0ErroCompilacao(String entrada) throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 6); // Status 6 = Compilation Error
        response.put("stdout", null);
        response.put("compile_output", "Main.java:5: error: ')' expected\n        System.out.println(a + b // ERRO\n                                          ^\n1 error");
        response.put("stderr", null);

        String responseJson = objectMapper.writeValueAsString(response);

        mockServer.expect(requestTo("https://judge0-ce.p.rapidapi.com/submissions?base64_encoded=false&wait=true"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("X-RapidAPI-Key", "a0901aebcbmsh9f8745681497560p1c9e05jsn6a7d905366c"))
                .andExpect(header("X-RapidAPI-Host", "judge0-ce.p.rapidapi.com"))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));
    }
}

