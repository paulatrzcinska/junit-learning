package learning;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

// by default, lifecycle is per method
@DisplayName("Computation class testing")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SomeClassTest {

    SomeClass someClass;
    TestInfo testInfo;
    TestReporter testReporter;

    // static won't be required if instance is created once per class
    @BeforeAll
    static void beforeAllInit() {
        System.out.println(("This needs to run before all"));
    }

    @BeforeEach
    void init(TestInfo testInfo, TestReporter testReporter) {
        this.testInfo = testInfo;
        this.testReporter = testReporter;
        someClass = new SomeClass();
        testReporter.publishEntry("Running " + testInfo.getDisplayName() + " with tags: " + testInfo.getTags());
    }

    @AfterEach
    void cleanUp() {
        System.out.println("Cleaning up...");
    }

    @Test
    @DisplayName("Testing add method")
    void testAdd() {
        int expected = 2;
        int actual = someClass.add(1, 1);

        assertEquals(expected, actual, "The add method should correctly add two numbers");
    }

    @Nested
    @DisplayName("Testing add method - set of tests")
    class AddTest {
        @Test
        @DisplayName("Testing add method - positive numbers")
        void testAddPositive() {
            assertEquals(2, someClass.add(1, 1), "The add method should correctly add two positive numbers");
        }

        @Test
        @DisplayName("Testing add method - negative numbers")
        void testAddNegative() {
            assertEquals(-2, someClass.add(-1, -1), "The add method should correctly add two negative numbers");
        }
    }

    @Test
    @DisplayName("Testing multiply method")
    @Tags({
            @Tag("Tag_to_test"),
            @Tag("Another_tag_to_test")
    })
    void testMultiply() {
        testReporter.publishEntry("Running " + testInfo.getDisplayName() + " with tags: " + testInfo.getTags());
        assertAll(
                () -> assertEquals(4, someClass.multiply(2, 2), "The multiply method should correctly multiply two numbers"),
                () -> assertEquals(0, someClass.multiply(2, 0), "The multiply method should correctly multiply two numbers"),
                () -> assertEquals(-2, someClass.multiply(2, -1), "The multiply method should correctly multiply two numbers")
        );
    }

    @Tag("Single")
    @Test
    @EnabledOnJre(JRE.JAVA_11)
    @DisplayName("Testing divide method")
    void testDivide() {
        boolean someConditional = false;
        /*
         information that something is assumed to run the particular test,
         if not true, the test won't be run
        */
        assumeTrue(someConditional);
        assertThrows(ArithmeticException.class,
                () -> someClass.divide(1, 0),
                "Divide by 0 should throw an exception");
    }

    @Tag("Repeated")
    @RepeatedTest(3)
    @DisabledOnOs(OS.LINUX)
    @DisplayName("Testing compute circle area method")
    void testComputeCircleArea(RepetitionInfo repetitionInfo) {
        if (repetitionInfo.getCurrentRepetition() != 2) {
            assertEquals(314.1592653589793,
                    someClass.computeCircleArea(10),
                    "Should return correct circle area");
        }
        else {
            fail("Repetition 2");
        }
    }

    @Test
    @Disabled
    @DisplayName("Example of failing test")
    void disabledTest() {
        fail("The test should be disabled");
    }
}