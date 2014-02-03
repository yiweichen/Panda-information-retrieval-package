/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ucl.panda.utility.analyzer;

import uk.ac.ucl.panda.utility.parser.TokenStream;
import uk.ac.ucl.panda.utility.parser.LowerCaseTokenizer;

import java.io.Reader;
import java.util.Hashtable;
import uk.ac.ucl.panda.utility.parser.StopFilter;

/**
 * PorterStemAnalyzer processes input
 * text by stemming English words to their roots.
 * This Analyzer also converts the input to lower case
 * and removes stop words.  A small set of default stop
 * words is defined in the STOP_WORDS
 * array, but a caller can specify an alternative set
 * of stop words by calling non-default constructor.
 */
public class PorterStemAnalyzer extends Analyzer
{
    private static Hashtable _stopTable;

    /**
     * An array containing some common English words
     * that are usually not useful for searching.
     */
    public static final String[] STOP_WORDS =
    {
    "x", "y", "your", "yours", "yourself", "yourselves", "you", "yond", "yonder", "yon", "ye", "yet", "z", "zillion", "j", "u", "umpteen", "usually", "us", "username", "uponed", "upons", "uponing", "upon", "ups", "upping", "upped", "up", "unto", "until", "unless", "unlike", "unliker", "unlikest", "under", "underneath", "use", "used", "usedest", "r", "rath", "rather", "rathest", "rathe", "re", "relate", "related", "relatively", "regarding", "really", "res", "respecting", "respectively", "q", "quite", "que", "qua", "n", "neither", "neaths", "neath", "nethe", "nethermost", "necessary", "necessariest", "necessarier", "never", "nevertheless", "nigh", "nighest", "nigher", "nine", "noone", "nobody", "nobodies", "nowhere", "nowheres", "no", "noes", "nor", "nos", "no-one", "none", "not", "notwithstanding", "nothings", "nothing", "nathless", "natheless", "t", "ten", "tills", "till", "tilled", "tilling", "to", "towards", "toward", "towardest", "towarder", "together", "too", "thy", "thyself", "thus", "than", "that", "those", "thou", "though", "thous", "thouses", "thoroughest", "thorougher", "thorough", "thoroughly", "thru", "thruer", "thruest", "thro", "through", "throughout", "throughest", "througher", "thine", "this", "thises", "they", "thee", "the", "then", "thence", "thenest", "thener", "them", "themselves", "these", "therer", "there", "thereby", "therest", "thereafter", "therein", "thereupon", "therefore", "their", "theirs", "thing", "things", "three", "two", "o", "oh", "owt", "owning", "owned", "own", "owns", "others", "other", "otherwise", "otherwisest", "otherwiser", "of", "often", "oftener", "oftenest", "off", "offs", "offest", "one", "ought", "oughts", "our", "ours", "ourselves", "ourself", "out", "outest", "outed", "outwith", "outs", "outside", "over", "overallest", "overaller", "overalls", "overall", "overs", "or", "orer", "orest", "on", "oneself", "onest", "ons", "onto", "a", "atween", "at", "athwart", "atop", "afore", "afterward", "afterwards", "after", "afterest", "afterer", "ain", "an", "any", "anything", "anybody", "anyone", "anyhow", "anywhere", "anent", "anear", "and", "andor", "another", "around", "ares", "are", "aest", "aer", "against", "again", "accordingly", "abaft", "abafter", "abaftest", "abovest", "above", "abover", "abouter", "aboutest", "about", "aid", "amidst", "amid", "among", "amongst", "apartest", "aparter", "apart", "appeared", "appears", "appear", "appearing", "appropriating", "appropriate", "appropriatest", "appropriates", "appropriater", "appropriated", "already", "always", "also", "along", "alongside", "although", "almost", "all", "allest", "aller", "allyou", "alls", "albeit", "awfully", "as", "aside", "asides", "aslant", "ases", "astrider", "astride", "astridest", "astraddlest", "astraddler", "astraddle", "availablest", "availabler", "available", "aughts", "aught", "vs", "v", "variousest", "variouser", "various", "via", "vis-a-vis", "vis-a-viser", "vis-a-visest", "viz", "very", "veriest", "verier", "versus", "k", "g", "go", "gone", "good", "got", "gotta", "gotten", "get", "gets", "getting", "b", "by", "byandby", "by-and-by", "bist", "both", "but", "buts", "be", "beyond", "because", "became", "becomes", "become", "becoming", "becomings", "becominger", "becomingest", "behind", "behinds", "before", "beforehand", "beforehandest", "beforehander", "bettered", "betters", "better", "bettering", "betwixt", "between", "beneath", "been", "below", "besides", "beside", "m", "my", "myself", "mucher", "muchest", "much", "must", "musts", "musths", "musth", "main", "make", "mayest", "many", "mauger", "maugre", "me", "meanwhiles", "meanwhile", "mostly", "most", "moreover", "more", "might", "mights", "midst", "midsts", "h", "huh", "humph", "he", "hers", "herself", "her", "hereby", "herein", "hereafters", "hereafter", "hereupon", "hence", "hadst", "had", "having", "haves", "have", "has", "hast", "hardly", "hae", "hath", "him", "himself", "hither", "hitherest", "hitherer", "his", "how-do-you-do", "however", "how", "howbeit", "howdoyoudo", "hoos", "hoo", "w", "woulded", "woulding", "would", "woulds", "was", "wast", "we", "wert", "were", "with", "withal", "without", "within", "why", "what", "whatever", "whateverer", "whateverest", "whatsoeverer", "whatsoeverest", "whatsoever", "whence", "whencesoever", "whenever", "whensoever", "when", "whenas", "whether", "wheen", "whereto", "whereupon", "wherever", "whereon", "whereof", "where", "whereby", "wherewithal", "wherewith", "whereinto", "wherein", "whereafter", "whereas", "wheresoever", "wherefrom", "which", "whichever", "whichsoever", "whilst", "while", "whiles", "whithersoever", "whither", "whoever", "whosoever", "whoso", "whose", "whomever", "s", "syne", "syn", "shalling", "shall", "shalled", "shalls", "shoulding", "should", "shoulded", "shoulds", "she", "sayyid", "sayid", "said", "saider", "saidest", "same", "samest", "sames", "samer", "saved", "sans", "sanses", "sanserifs", "sanserif", "so", "soer", "soest", "sobeit", "someone", "somebody", "somehow", "some", "somewhere", "somewhat", "something", "sometimest", "sometimes", "sometimer", "sometime", "several", "severaler", "severalest", "serious", "seriousest", "seriouser", "senza", "send", "sent", "seem", "seems", "seemed", "seemingest", "seeminger", "seemings", "seven", "summat", "sups", "sup", "supping", "supped", "such", "since", "sine", "sines", "sith", "six", "stop", "stopped", "p", "plaintiff", "plenty", "plenties", "please", "pleased", "pleases", "per", "perhaps", "particulars", "particularly", "particular", "particularest", "particularer", "pro", "providing", "provides", "provided", "provide", "probably", "l", "layabout", "layabouts", "latter", "latterest", "latterer", "latterly", "latters", "lots", "lotting", "lotted", "lot", "lest", "less", "ie", "ifs", "if", "i", "info", "information", "itself", "its", "it", "is", "idem", "idemer", "idemest", "immediate", "immediately", "immediatest", "immediater", "in", "inwards", "inwardest", "inwarder", "inward", "inasmuch", "into", "instead", "insofar", "indicates", "indicated", "indicate", "indicating", "indeed", "inc", "f", "fact", "facts", "fs", "figupon", "figupons", "figuponing", "figuponed", "few", "fewer", "fewest", "frae", "from", "failing", "failings", "five", "furthers", "furtherer", "furthered", "furtherest", "further", "furthering", "furthermore", "fourscore", "followthrough", "for", "forwhy", "fornenst", "formerly", "former", "formerer", "formerest", "formers", "forbye", "forby", "fore", "forever", "forer", "fores", "four", "d", "ddays", "dday", "do", "doing", "doings", "doe", "does", "doth", "downwarder", "downwardest", "downward", "downwards", "downs", "done", "doner", "dones", "donest", "dos", "dost", "did", "differentest", "differenter", "different", "describing", "describe", "describes", "described", "despiting", "despites", "despited", "despite", "during", "c", "cum", "circa", "chez", "cer", "certain", "certainest", "certainer", "cest", "canst", "cannot", "cant", "cants", "canting", "cantest", "canted", "co", "could", "couldst", "comeon", "comeons", "come-ons", "come-on", "concerning", "concerninger", "concerningest", "consequently", "considering", "e", "eg", "eight", "either", "even", "evens", "evenser", "evensest", "evened", "evenest", "ever", "everyone", "everything", "everybody", "everywhere", "every", "ere", "each", "et", "etc", "elsewhere", "else", "ex", "excepted", "excepts", "except", "excepting", "exes", "enough"
 };

    /**
     * Builds an analyzer.
     */
    public PorterStemAnalyzer()
    {
        this(STOP_WORDS);
    }

    /**
     * Builds an analyzer with the given stop words.
     *
     * @param stopWords a String array of stop words
     */
    public PorterStemAnalyzer(String[] stopWords)
    {
        _stopTable = StopFilter.makeStopTable(stopWords);
    }

    /**
     * Processes the input by first converting it to
     * lower case, then by eliminating stop words, and
     * finally by performing Porter stemming on it.
     *
     * @param reader the Reader that
     *               provides access to the input text
     * @return an instance of TokenStream
     */
    public final TokenStream tokenStream(Reader reader)
    {
        return new PorterStemFilter(
            new StopFilter(new LowerCaseTokenizer(reader),
                _stopTable));
    }

    @Override
    public TokenStream tokenStream(String fieldName, Reader reader) {
        return new PorterStemFilter(
            new StopFilter(new LowerCaseTokenizer(reader),
                _stopTable));
    }
}