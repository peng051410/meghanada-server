package meghanada.docs.declaration;

import meghanada.GradleTestBase;
import meghanada.reflect.asm.CachedASMReflector;
import org.junit.Test;

import java.io.File;
import java.util.Optional;

import static meghanada.config.Config.debugIt;
import static meghanada.config.Config.timeIt;
import static org.junit.Assert.*;

public class DeclarationSearcherTest extends GradleTestBase {

    private static DeclarationSearcher searcher;

    @org.junit.BeforeClass
    public static void beforeClass() throws Exception {
        GradleTestBase.setupReflector();
        CachedASMReflector cachedASMReflector = CachedASMReflector.getInstance();
        cachedASMReflector.addClasspath(getOutputDir());
        cachedASMReflector.createClassIndexes();
    }

    private static DeclarationSearcher getSearcher() throws Exception {
        if (searcher != null) {
            return searcher;
        }
        searcher = new DeclarationSearcher(getProject());
        return searcher;
    }

    @Test
    public void testFieldDeclaration01() throws Exception {
        File f = new File("./src/main/java/meghanada/server/emacs/EmacsServer.java").getCanonicalFile();
        assert f.exists();

        final DeclarationSearcher searcher = getSearcher();
        final Optional<Declaration> result = timeIt(() -> searcher.searchDeclaration(f, 57, 18, "executorService"));
        assertNotNull(result);
        assertTrue(result.isPresent());
        result.ifPresent(declaration -> {
            assertEquals("this.executorService", declaration.scopeInfo);
            assertEquals("java.util.concurrent.ExecutorService", declaration.signature);
        });
    }

    @Test
    public void testMethodDeclaration01() throws Exception {
        File f = new File("./src/main/java/meghanada/server/emacs/EmacsServer.java").getCanonicalFile();
        assert f.exists();
        final DeclarationSearcher searcher = getSearcher();
        final Optional<Declaration> result = debugIt(() -> {
            return searcher.searchDeclaration(f, 46, 49, "getByName");
        });
        assertNotNull(result);
        assertTrue(result.isPresent());
        result.ifPresent(declaration -> {
            assertEquals("InetAddress.getByName", declaration.scopeInfo);
            assertEquals("public static InetAddress getByName(String arg0) throws UnknownHostException", declaration.signature);
        });
    }

    @Test
    public void testMethodDeclaration02() throws Exception {
        File f = new File("./src/main/java/meghanada/server/emacs/EmacsServer.java").getCanonicalFile();
        assert f.exists();
        final DeclarationSearcher searcher = getSearcher();
        final Optional<Declaration> result = debugIt(() -> {
            return searcher.searchDeclaration(f, 106, 37, "submit");
        });
        assertNotNull(result);
        assertTrue(result.isPresent());
        result.ifPresent(declaration -> {
            assertEquals("this.executorService.submit", declaration.scopeInfo);
            assertEquals("public Future<?> submit(Runnable arg0)", declaration.signature);
        });
    }

    @Test
    public void testMethodDeclaration03() throws Exception {
        File f = new File("./src/main/java/meghanada/server/emacs/EmacsServer.java").getCanonicalFile();
        assert f.exists();
        final DeclarationSearcher searcher = getSearcher();
        final Optional<Declaration> result = debugIt(() -> {
            return searcher.searchDeclaration(f, 107, 52, "BufferedReader");
        });
        assertNotNull(result);
        assertTrue(result.isPresent());
        result.ifPresent(declaration -> {
            assertEquals("BufferedReader", declaration.scopeInfo);
            assertEquals("public BufferedReader(Reader arg0)", declaration.signature);
        });
    }

    @Test
    public void testClassDeclaration01() throws Exception {
        File f = new File("./src/main/java/meghanada/server/emacs/EmacsServer.java").getCanonicalFile();
        assert f.exists();
        final DeclarationSearcher searcher = getSearcher();
        final Optional<Declaration> result = debugIt(() -> {
            return searcher.searchDeclaration(f, 30, 20, "ServerSocket");
        });
        assertNotNull(result);
        assertTrue(result.isPresent());
        result.ifPresent(declaration -> {
            assertEquals("ServerSocket", declaration.scopeInfo);
            assertEquals("java.net.ServerSocket", declaration.signature);
        });
    }

}