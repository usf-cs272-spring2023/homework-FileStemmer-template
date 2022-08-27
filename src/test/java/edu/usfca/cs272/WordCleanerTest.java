package edu.usfca.cs272;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.TagFilter;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

/**
 * Tests of the {@link WordCleaner} class.
 *
 * @see WordCleaner
 *
 * @author CS 272 Software Development (University of San Francisco)
 * @version Fall 2022
 */
@TestMethodOrder(MethodName.class)
public class WordCleanerTest {
	/** Path to the test resources. */
	public static final Path BASE_PATH = Path.of("src", "test", "resources");

	/**
	 * Collection of tests.
	 *
	 * @see WordCleaner#listStems(String)
	 * @see WordCleaner#listStems(String, opennlp.tools.stemmer.Stemmer)
	 */
	@Nested
	@TestMethodOrder(OrderAnnotation.class)
	public class A_ListStemsTests {
		/**
		 * Tests expected output for given test case.
		 *
		 * @param line the line to stem
		 * @param output the expected output
		 */
		public void test(String line, String[] output) {
			List<String> expected = Arrays.stream(output).toList();
			List<String> actual = WordCleaner.listStems(line);
			assertEqualsJoined(expected, actual,
					"Lists do not match; use compare feature in Eclipse for details.");
		}

		// Test cases from: http://snowballstem.org/algorithms/english/stemmer.html
		// Right-click individual test methods to run only that test.

		/**
		 * Runs a single test case.
		 */
		@Test
		@Order(1)
		public void testOneWord() {
			String line = "conspicuously";
			String[] output = { "conspicu" };
			test(line, output);
		}

		/**
		 * Runs a single test case.
		 */
		@Test
		@Order(2)
		public void testEmpty() {
			test("", new String[] {});
		}

		/**
		 * Runs a single test case.
		 */
		@Test
		@Order(3)
		public void testOrder() {
			test("cat cat bat ant", new String[] { "cat", "cat", "bat", "ant" });
		}

		/**
		 * Runs a single test case.
		 */
		@Test
		@Order(4)
		public void testGroupOne() {
			String[] input = {
					"consign", "consigned", "consigning", "consignment", "consist",
					"consisted", "consistency", "consistent", "consistently",
					"consisting", "consists", "consolation", "consolations",
					"consolatory", "console", "consoled", "consoles", "consolidate",
					"consolidated", "consolidating", "consoling", "consolingly",
					"consols", "consonant", "consort", "consorted", "consorting",
					"conspicuous", "conspicuously", "conspiracy", "conspirator",
					"conspirators", "conspire", "conspired", "conspiring", "constable",
					"constables", "constance", "constancy", "constant" };

			String[] output = {
					"consign", "consign", "consign", "consign", "consist", "consist",
					"consist", "consist", "consist", "consist", "consist", "consol",
					"consol", "consolatori", "consol", "consol", "consol", "consolid",
					"consolid", "consolid", "consol", "consol", "consol", "conson",
					"consort", "consort", "consort", "conspicu", "conspicu", "conspiraci",
					"conspir", "conspir", "conspir", "conspir", "conspir", "constabl",
					"constabl", "constanc", "constanc", "constant" };

			String line = String.join(", ", input);
			test(line, output);
		}

		/**
		 * Runs a single test case.
		 */
		@Test
		@Order(5)
		public void testGroupTwo() {
			String[] input = {
					"KNACK", "KNACKERIES", "KNACKS", "KNAG", "KNAVE", "KNAVES", "KNAVISH",
					"KNEADED", "KNEADING", "KNEE", "KNEEL", "KNEELED", "KNEELING",
					"KNEELS", "KNEES", "KNELL", "KNELT", "KNEW", "KNICK", "KNIF", "KNIFE",
					"KNIGHT", "KNIGHTLY", "KNIGHTS", "KNIT", "KNITS", "KNITTED",
					"KNITTING", "KNIVES", "KNOB", "KNOBS", "KNOCK", "KNOCKED", "KNOCKER",
					"KNOCKERS", "KNOCKING", "KNOCKS", "KNOPP", "KNOT", "KNOTS" };

			String[] output = {
					"knack", "knackeri", "knack", "knag", "knave", "knave", "knavish",
					"knead", "knead", "knee", "kneel", "kneel", "kneel", "kneel", "knee",
					"knell", "knelt", "knew", "knick", "knif", "knife", "knight",
					"knight", "knight", "knit", "knit", "knit", "knit", "knive", "knob",
					"knob", "knock", "knock", "knocker", "knocker", "knock", "knock",
					"knopp", "knot", "knot" };

			String line = String.join(" **** ", input);
			test(line, output);
		}
	}

	/**
	 * Collection of tests.
	 *
	 * @see WordCleaner#uniqueStems(String)
	 * @see WordCleaner#uniqueStems(String, opennlp.tools.stemmer.Stemmer)
	 */
	@Nested
	@TestMethodOrder(OrderAnnotation.class)
	public class B_UniqueStemsTests extends A_ListStemsTests {
		@Override
		public void test(String line, String[] output) {
			// converts to list for ordering purposes
			List<String> expected = Arrays.stream(output)
					.sorted()
					.distinct()
					.toList();
			List<String> actual = WordCleaner.uniqueStems(line).stream().toList();
			assertEqualsJoined(expected, actual,
					"Sets do not match; use compare feature in Eclipse for details.");
		}
	}

	/**
	 * Collection of tests.
	 *
	 * @see WordCleaner#listStems(Path)
	 */
	@Nested
	@TestMethodOrder(OrderAnnotation.class)
	public class C_ListStemFileTests {
		/**
		 * Tests expected output for given test case.
		 *
		 * @param path the file path to stem
		 * @param output the expected output
		 * @throws IOException if I/O error occurs
		 */
		public void test(Path path, String[] output) throws IOException {
			List<String> expected = Arrays.stream(output).toList();
			List<String> actual = WordCleaner.listStems(path);
			assertEqualsJoined(expected, actual,
					"Lists do not match; use compare feature in Eclipse for details.");
		}

		/**
		 * Runs a single test case.
		 *
		 * @throws IOException if I/O error occurs
		 */
		@Test
		@Order(1)
		public void testCleaner() throws IOException {
			Path path = BASE_PATH.resolve("cleaner.txt");
			String[] output = {
					"okapi", "okapi", "mongoos", "lori", "lori", "lori", "axolotl",
					"narwhal", "platypus", "echidna", "tarsier", "antelop", "antelop",
					"antelop", "antelop", "antelop", "antelop", "antelop", "antelop",
					"antelop", "antelop", "observa", "observ", "observacion", "observ",
					"observ", "observ", "observ", "observ", "observ", "observ", "observ",
					"observ", "observ" };
			test(path, output);
		}

		/**
		 * Runs a single test case.
		 *
		 * @throws IOException if I/O error occurs
		 */
		@Test
		@Order(2)
		public void testStems() throws IOException {
			Path input = BASE_PATH.resolve("stem-in.txt");
			Path output = BASE_PATH.resolve("stem-out.txt");
			String[] expected = WordCleaner.parse(Files.readString(output, UTF_8));
			test(input, expected);
		}
	}

	/**
	 * Collection of tests.
	 *
	 * @see WordCleaner#uniqueStems(Path)
	 */
	@Nested
	@TestMethodOrder(OrderAnnotation.class)
	public class D_UniqueStemFileTests extends C_ListStemFileTests {
		@Override
		public void test(Path path, String[] output) throws IOException {
			// converts to list for ordering purposes
			List<String> expected = Arrays.stream(output)
					.sorted()
					.distinct()
					.toList();
			List<String> actual = WordCleaner.uniqueStems(path).stream().toList();
			assertEqualsJoined(expected, actual,
					"Sets do not match; use compare feature in Eclipse for details.");
		}
	}

	/**
	 * Collection of tests.
	 *
	 * @see WordCleaner#listUniqueStems(Path)
	 */
	@Nested
	@TestMethodOrder(OrderAnnotation.class)
	public class E_ListUniqueStemTests {
		/**
		 * Tests expected output for given test case.
		 *
		 * @param expected the expected result
		 * @param actual the actual result
		 */
		public void test(List<? extends Collection<String>> expected,
				ArrayList<TreeSet<String>> actual) {
			List<String> expectedJoined = expected.stream()
					.map(Collection::toString)
					.toList();
			List<String> actualJoined = actual.stream().map(Set::toString).toList();
			assertEqualsJoined(expectedJoined, actualJoined,
					"Lines do not match; use compare feature in Eclipse for details.");
		}

		/**
		 * Runs a single test case.
		 *
		 * @throws IOException if I/O error occurs
		 */
		@Test
		@Order(1)
		public void testCleaner() throws IOException {
			Path input = BASE_PATH.resolve("cleaner.txt");

			List<List<String>> expected = List.of(List.of("lori", "mongoos", "okapi"),
					List.of("axolotl", "echidna", "narwhal", "platypus", "tarsier"),
					List.of(), List.of("antelop"), List.of("antelop"), List.of(),
					List.of("observ", "observa", "observacion"), List.of("observ"));

			ArrayList<TreeSet<String>> actual = WordCleaner.listUniqueStems(input);
			test(expected, actual);
		}
	}

	/**
	 * Attempts to check for issues with the approach.
	 */
	@Nested
	@Tag("approach")
	public class F_ApproachTests {
		/**
		 * Checks to see if the File class was imported.
		 */
		@Test
		public void testFileImport() {
			String regex = "(?is)\\bimport\\s+java.io.File\\s*;";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(source);
			Assertions.assertFalse(matcher.find(),
					"Do not use the java.io.File class in your code!");
		}

		/**
		 * Checks to see if try-with-resources was used
		 */
		@Test
		public void testTryWithResources() {
			String regex = "(?is)\\btry\\s*\\([^}]+\\)";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(source);
			Assertions.assertTrue(matcher.find(),
					"Make sure you use try-with-resources in your code!");
		}

		/**
		 * Checks that methods throw exceptions as expected
		 */
		@Test
		public void testThrowsNullExceptions() {
			Assertions.assertThrows(NullPointerException.class, () -> {
				Path nullPath = null;
				WordCleaner.listStems(nullPath);
			});
		}

		/**
		 * Checks that methods throw exceptions as expected
		 */
		@Test
		public void testThrowsDirectoryExceptions() {
			Assertions.assertThrows(IOException.class, () -> {
				Path nullPath = Path.of("src");
				WordCleaner.listStems(nullPath);
			});
		}

		/**
		 * Checks that methods throw exceptions as expected
		 */
		@Test
		public void testThrowsNoFileExceptions() {
			Assertions.assertThrows(IOException.class, () -> {
				Path nullPath = Path.of("nowhere");
				WordCleaner.uniqueStems(nullPath);
			});
		}

		/**
		 * Causes this group of tests to fail if the other non-approach tests are
		 * not yet passing.
		 */
		@Test
		public void testOthersPassing() {
			var request = LauncherDiscoveryRequestBuilder.request()
					.selectors(DiscoverySelectors.selectClass(WordCleanerTest.class))
					.filters(TagFilter.excludeTags("approach"))
					.build();

			var launcher = LauncherFactory.create();
			var listener = new SummaryGeneratingListener();

			Logger logger = Logger.getLogger("org.junit.platform.launcher");
			logger.setLevel(Level.SEVERE);

			launcher.registerTestExecutionListeners(listener);
			launcher.execute(request);

			Assertions.assertEquals(0, listener.getSummary().getTotalFailureCount(),
					"Must pass other tests to earn credit for approach group!");
		}

		/** The source code for TextFileStemmer. */
		private String source;

		/**
		 * Loads the entire source code as a String object.
		 *
		 * @throws IOException if an IO error occurs
		 */
		@BeforeEach
		public void setup() throws IOException {
			Path java = Path.of("src", "main", "java");
			Path cs272 = Path.of("edu", "usfca", "cs272");
			String file = WordCleaner.class.getSimpleName() + ".java";

			Path path = java.resolve(cs272).resolve(file);
			source = Files.readString(path, UTF_8);
		}
	}

	/**
	 * Collection of tests.
	 *
	 * These tests should already pass, since the methods are given for you
	 * already. They are not included in the homework grade.
	 *
	 * @see WordCleaner#split(String)
	 * @see WordCleaner#clean(String)
	 * @see WordCleaner#parse(String)
	 */
	@Nested
	@TestMethodOrder(OrderAnnotation.class)
	public class G_SplitCleanParseTests {
		/**
		 * Tests the {@link WordCleaner#clean(String)} method.
		 */
		@Nested
		@TestMethodOrder(OrderAnnotation.class)
		public class CleanTestCases {
			/**
			 * Calls {@link WordCleaner#clean(String)} on the supplied text, and makes
			 * sure it matches the expected text.
			 *
			 * @param text the text to clean
			 * @param expected the cleaned output
			 */
			public void test(String text, String expected) {
				String actual = WordCleaner.clean(text);
				assertEquals(expected, actual);
			}

			/**
			 * Runs {@link #test(String, String)} with the supplied test case.
			 */
			@Test
			@Order(1)
			public void helloWorld() {
				test("hello world", "hello world");
			}

			/**
			 * Runs {@link #test(String, String)} with the supplied test case.
			 */
			@Test
			@Order(2)
			public void withTabs() {
				test("\t hello  world ", "\t hello  world ");
			};

			/**
			 * Runs {@link #test(String, String)} with the supplied test case.
			 */
			@Test
			@Order(3)
			public void withPunctuation() {
				test("hello, world!", "hello world");
			}

			/**
			 * Runs {@link #test(String, String)} with the supplied test case.
			 */
			@Test
			@Order(4)
			public void withDigit() {
				test("hello 1 world", "hello  world");
			}

			/**
			 * Runs {@link #test(String, String)} with the supplied test case.
			 */
			@Test
			@Order(5)
			public void withSymbol() {
				test("hello @world", "hello world");
			}

			/**
			 * Runs {@link #test(String, String)} with the supplied test case.
			 */
			@Test
			@Order(6)
			public void withUppercase() {
				test("HELLO WORLD", "hello world");
			}

			/**
			 * Runs {@link #test(String, String)} with the supplied test case.
			 */
			@Test
			@Order(7)
			public void withExclamation() {
				test("¡Hello world!", "hello world");
			}

			/**
			 * Runs {@link #test(String, String)} with the supplied test case.
			 */
			@Test
			@Order(8)
			public void withMarks() {
				test("héḶlõ ẁörld", "hello world");
			}

			/**
			 * Runs {@link #test(String, String)} with the supplied test case.
			 */
			@Test
			@Order(9)
			public void onlySpaces() {
				test("   ", "   ");
			}

			/**
			 * Runs {@link #test(String, String)} with the supplied test case.
			 */
			@Test
			@Order(10)
			public void onlyDigits() {
				test("1234567890", "");
			}
		}

		/**
		 * Tests the {@link WordCleaner#parse(String)} method.
		 */
		@Nested
		@TestMethodOrder(OrderAnnotation.class)
		public class ParseTextTests {
			/**
			 * Calls {@link WordCleaner#parse(String)} on the supplied text, and makes
			 * sure it matches the expected array.
			 *
			 * @param text the text to parse
			 */
			public void test(String text) {
				String[] expected = new String[] { "hello", "world" };
				assertArrayEquals(expected, WordCleaner.parse(text));
			}

			/**
			 * Runs {@link #test(String)} with the supplied test case.
			 */
			@Test
			@Order(1)
			public void helloWorld() {
				test("hello world");
			}

			/**
			 * Runs {@link #test(String)} with the supplied test case.
			 */
			@Test
			@Order(2)
			public void withTabs() {
				test("\t hello  world ");
			}

			/**
			 * Runs {@link #test(String)} with the supplied test case.
			 */
			@Test
			@Order(3)
			public void withPunctuation() {
				test("hello, world!");
			}

			/**
			 * Runs {@link #test(String)} with the supplied test case.
			 */
			@Test
			@Order(4)
			public void withDigit() {
				test("hello 1 world");
			}

			/**
			 * Runs {@link #test(String)} with the supplied test case.
			 */
			@Test
			@Order(5)
			public void withSymbol() {
				test("hello @world");
			}

			/**
			 * Runs {@link #test(String)} with the supplied test case.
			 */
			@Test
			@Order(6)
			public void withUppercase() {
				test("HELLO WORLD");
			}

			/**
			 * Runs {@link #test(String)} with the supplied test case.
			 */
			@Test
			@Order(7)
			public void withExclamation() {
				test("¡Hello world!");
			}

			/**
			 * Runs {@link #test(String)} with the supplied test case.
			 */
			@Test
			@Order(8)
			public void withMarks() {
				test("héḶlõ ẁörld");
			}
		}

		/**
		 * Tests the {@link WordCleaner#parse(String)} method.
		 */
		@Nested
		@TestMethodOrder(OrderAnnotation.class)
		public class ParseTextEmptyTests {
			/**
			 * Calls {@link WordCleaner#parse(String)} on the supplied text, and makes
			 * sure it matches an empty array.
			 *
			 * @param text the text to parse
			 */
			public void test(String text) {
				String[] expected = new String[0];
				assertArrayEquals(expected, WordCleaner.parse(text));
			}

			/**
			 * Runs {@link #test(String)} with the supplied test case.
			 */
			@Test
			@Order(1)
			public void singleSpace() {
				test(" ");
			}

			/**
			 * Runs {@link #test(String)} with the supplied test case.
			 */
			@Test
			@Order(2)
			public void emptyString() {
				test("");
			}

			/**
			 * Runs {@link #test(String)} with the supplied test case.
			 */
			@Test
			@Order(3)
			public void onlyDigits() {
				test("1234567890");
			}

			/**
			 * Runs {@link #test(String)} with the supplied test case.
			 */
			@Test
			@Order(4)
			public void mixedSymbols() {
				test("\t 11@ ");
			}
		}
	}

	/**
	 * Tests if two collections are equal by converting them to text with one item
	 * per line. Results in nicer side-by-side output in Eclipse JUnit view.
	 *
	 * @param expected the expected result
	 * @param actual the actual result
	 * @param debug the debug string to display
	 */
	public static void assertEqualsJoined(Collection<String> expected,
			Collection<String> actual, String debug) {
		String expectedJoined = String.join("\n", expected);
		String actualJoined = String.join("\n", actual);
		assertEquals(expectedJoined, actualJoined, debug + "\n");
	}
}
