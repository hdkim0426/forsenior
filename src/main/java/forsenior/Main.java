package forsenior;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


import org.snu.ids.ha.ma.Sentence;

import javazoom.jlgui.basicplayer.BasicPlayer;

public class Main {
	
	 public static void MatrixTime(int delayTime){
	       long saveTime = System.currentTimeMillis();
	       long currTime = 0;
	       while( currTime - saveTime < delayTime){
	           currTime = System.currentTimeMillis();
	       }
	   }
	

	public static void main(String... args) {
		
		connFindFromDB db=new connFindFromDB();
		List<List<String>> dbData = db.findReply();
		db.getConnection();
		List<List<String>> questionData = db.questionFirst();


		String credentials = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");

		if (credentials == null || !new File(credentials).exists()) {
			System.err.println("Please set correct environment variable GOOGLE_APPLICATION_CREDENTIALS");
			System.exit(1);
		}

		try (InputStream is = ClassLoader.getSystemResourceAsStream("forsenior.properties")) {
			Properties prop = new Properties();
			prop.load(is);
			TextToSpeech tts = new TextToSpeech(prop.getProperty("ttsid"), prop.getProperty("ttssecret"));
			
			while(true){
				boolean weatherflag = false;
				// recording (녹음하여 파일로 저장)
				Recoder recoder = new Recoder(10000, new File("b.wav"));
				File recordedFile = recoder.record();
				System.out.println("Recorded file: " + recordedFile.getAbsolutePath());
			
			
				// result of recording -> String (녹음파일의 내용을 문자열 리스트 형태로 저장)
				List<String> textList = SpeechToText.process(recordedFile.getAbsolutePath());
				
				
				// Morpheme analying (형태소 분석)
				//List<Sentence> sentenceList; //=MorphemeAnalyzer.analyze(textList);
				
				MorphemeAnalyzer m=new MorphemeAnalyzer();
				if (textList.size()!=0){
					System.out.println("형태소 분석 시작!");
					MorphemeAnalyzer.analyze(textList.get(0).toString());
				}
				
				//질문이 입력되지 않는 경우, 먼저 질문
				else{
					int questionrange1=0,questionrange2=3;
					System.out.println("녹음되지 않았습니다.");
					System.out.println("곧 질문이 나옵니다.");
					String response = tts.process(String.join(", ", questionData.get((int)(Math.random() * (questionrange2 + 1)) + questionrange1)));
					System.out.println("Speech file: " + response);
					
					// Play speech file
					BasicPlayer player = new BasicPlayer();
					player.open(new File(response));
					player.play();
					
					MatrixTime(10000);
					continue;
				}
				
				//키워드 가져오기
				System.out.println("keyword: " + m.getKeyword());

				//질문의 키워드를 DB의 모든 질문과 비교해가며 유사도 산출 및 유사도가 가장 높은 인덱스 구하기
				int[] similarity = new int[dbData.size()];
				Arrays.fill(similarity, 0);
				
				for(int i=0;i<dbData.size();i++){
					for(int j=0;j<m.getKeyword().size();j++)
					{
						if(dbData.get(i).get(0).contains(m.getKeyword().get(j))){
							similarity[i]++;
						}
									
					}				
				}
				int max=0;
				for(int i=0;i<dbData.size();i++){
					if(similarity[max]<similarity[i])
					{
						max=i;
					}
				}
				
				//유사도 출력
				/*
				for(int i=0;i<dbData.size();i++){
					System.out.println(dbData.get(i) + "의 유사도 : " + similarity[i]);
				}
				*/
				
							
				int sumofsimilarity=0;
				for(int i=0;i<similarity.length;i++){
					sumofsimilarity = sumofsimilarity + similarity[i];
				}
				String text="";
				if(sumofsimilarity == 0){
					System.out.println("처음 듣는 말이에요.공부해둘게요! ");
					text="처음 듣는 말이에요.공부해둘게요! ";
					
					// textList
					//db넣기
					db.getConnection();
					db.insertNewQuestion(textList);
					
				}
				else{
					System.out.println("유사도가 가장 큰 DB의 행 " + dbData.get(max) + ", 유사도 : " + similarity[max]);
					
					System.out.println("질문의 카테고리 분류 결과 : " + m.getInputCategories());
					//답변 (질문형 대답, 일반형 대답 중 랜덤 선택)
					int replyrange1=2, replyrange2=3;
					text = String.join(", ", dbData.get(max).get((int)(Math.random() * (replyrange2 - replyrange1 + 1)) + replyrange1));
					
					System.out.println("reply from db : " + text);
					
					//날씨 정보 가져오기. (기상청 XML api를 파싱하여 19항목의 기상 정보 중 현재부터 약 3시간 동안의 온도, 날씨를 가져옴)
					Weather w = new Weather();
					String wAnswer=" "+w.getHour()[0]+" 의 온도는 "+w.getTemp()[0]+" 도 이고, 날씨는 "+w.getKor()[0]+"입니다.";
					
					
					//입력된 질문의 카테고리가 날씨라면, 날씨정보를 답변에 추가
					if(m.getInputCategories().equals("날씨")){
						weatherflag=true;
						text = text + wAnswer;
						System.out.println(w.getHour()[0] + ": "+ w.getTemp()[0]+ "도 " + w.getKor()[0]);
					}
				}
				
				m.initKeyword();
				
				//reply(text format) -> reply(speech file)
				String response = tts.process(text);
				System.out.println("Speech file: " + response);
				
				// Play speech file
				BasicPlayer player = new BasicPlayer();
				player.open(new File(response));
				player.play();
				
				if(weatherflag==true){
					MatrixTime(13000);
				}
				else{
					MatrixTime(7000);
				}
				
			}


		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
