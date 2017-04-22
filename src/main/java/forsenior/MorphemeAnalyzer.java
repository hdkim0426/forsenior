package forsenior;

import java.util.List;

import org.snu.ids.ha.ma.MExpression;
import org.snu.ids.ha.ma.Sentence;

public class MorphemeAnalyzer {
	public static void analyze(String string) {
		try {
			// init MorphemeAnalyzer
			org.snu.ids.ha.ma.MorphemeAnalyzer ma = new org.snu.ids.ha.ma.MorphemeAnalyzer();

			// create logger, null then System.out is set as a default logger
			ma.createLogger(null);

			// analyze morpheme without any post processing 
			List<MExpression> ret = ma.analyze(string);

			// refine spacing
			ret = ma.postProcess(ret);

			// leave the best analyzed result
			ret = ma.leaveJustBest(ret);

			// divide result to setences
			List<Sentence> stl = ma.divideToSentences(ret);

			// print the result
			for( int i = 0; i < stl.size(); i++ ) {
				Sentence st = stl.get(i);
				System.out.println("===>  " + st.getSentence());
				for( int j = 0; j < st.size(); j++ ) {
					System.out.println(st.get(j));
				}
			}

			ma.closeLogger();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
