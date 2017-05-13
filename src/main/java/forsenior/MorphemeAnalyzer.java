package forsenior;

import java.util.ArrayList;
import java.util.List;

import org.snu.ids.ha.ma.MExpression;
import org.snu.ids.ha.ma.Sentence;

public class MorphemeAnalyzer {

	public static List<Sentence> stl;
	public static List<String> keyword = new ArrayList<>();
	public static List<String> tempkeyword = new ArrayList<>();
	public static String inputcategories;
	
	public static void analyze(String string) {
		
		try {
			// init MorphemeAnalyzer
			org.snu.ids.ha.ma.MorphemeAnalyzer ma = new org.snu.ids.ha.ma.MorphemeAnalyzer();

			// create logger, null then System.out is set as a default logger
			//ma.createLogger(null);

			// analyze morpheme without any post processing 
			List<MExpression> ret = ma.analyze(string);

			// refine spacing
			ret = ma.postProcess(ret);

			// leave the best analyzed result
			ret = ma.leaveJustBest(ret);

			// divide result to setences
			stl= ma.divideToSentences(ret);
			// print the result
			
			
			//형태소 분석 결과 출력 및 명사와 동사를 골라내서 키워드 추출			
			for( int i = 0; i < stl.size(); i++ ) {
				Sentence st = stl.get(i);
				System.out.println("===>  " + st.getSentence());

				for( int j = 0; j < st.size(); j++ ) {
					System.out.println(st.get(j));
					if(st.get(j).toString().contains("NN") || st.get(j).toString().contains("NR") || st.get(j).toString().contains("NP") ||
					   st.get(j).toString().contains("VV") || st.get(j).toString().contains("VA") || st.get(j).toString().contains("MAG"))
					{
						tempkeyword.add(st.get(j).toString());
					}

				}
				
				for( int k = 0; k < tempkeyword.size(); k++ )
				{
					keyword.add(tempkeyword.get(k).split("/")[1]);
				}
				
				// 카테고리 분류하기
				if(keyword.contains("날씨") || keyword.contains("비") || keyword.contains("뼈마디") || keyword.contains("비가"))
				{
					inputcategories = "날씨";
				}
				
				else if(keyword.contains("건강") || keyword.contains("나이") || keyword.contains("아파") || keyword.contains("아픈") || keyword.contains("힘들"))
				{
					inputcategories = "건강";
				}
				
				else if(keyword.contains("아들") || keyword.contains("딸") || keyword.contains("손자") || keyword.contains("학교") || keyword.contains("가족") ||(keyword.contains("명절") && keyword.contains("누")))
				{
					inputcategories = "가족";
				}
				
				else
				{
					inputcategories = "고민";
				}
					
								
			}
			//ma.closeLogger();
			
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
	}
	public List<String> getKeyword() {
	  return keyword;
	}
	public void initKeyword() {
	  /*
	  for(int i=0;i<keyword.size();i++){
		  keyword.set(i, null);
	  }
	  */
		keyword = new ArrayList<>();
		tempkeyword = new ArrayList<>();

	}
	public String getInputCategories() {
	  return inputcategories;
	}
}
