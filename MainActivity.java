package com.icon.softwareengineeringdepartment.cbnu.testsendemailforimage;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by 석현 on 2015-12-28.
 * /*******주의할점*********
 * gmail 경우 안전하지 않은 앱의 접근을 막아 놓는데 풀면 어떻게든 됨.
 */


public class MainActivity extends AppCompatActivity {
    LinearLayout layout; // 현재 화면을 감싸고 있는 레이아웃 설정
    String dirPath; // 이미지 파일이 저장될 디렉토리
    Button btn; // 테스트 버튼
    String dirpathAndFileName; // 파일과파일이름 합칩
    String mailsubject; // 보낼 메일 제목
    String mailcontent; // 보낼 메일 내용
    EditText mailaddress;// 테스트 뷰

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn =(Button) findViewById(R.id.sendbtn);
        layout = (LinearLayout)findViewById(R.id.screen);
        mailaddress = (EditText)findViewById(R.id.subjectText);
        btn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                try {
                    View v1 = layout.getRootView();
                    v1.setDrawingCacheEnabled(true);
                    Bitmap bm = v1.getDrawingCache();
                    String mailaddressString = "여기는 보낼 메일 주소가 들어가야해요";
                    mailaddressString = mailaddress.getText().toString();
                    String filename = "파일제목입니다.";

                    screenshot(bm, mailaddressString);
                    Toast.makeText(MainActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();

                    mailsubject = "메일 제목"; //버튼이 눌렸을때 가져오면됨
                    mailcontent = "내용은 안나오나"; // 위와 동일

                    String[] fileproperty = {mailsubject,mailcontent,dirpathAndFileName,mailaddressString,filename};
                    //순서대로 메일제목, 메일 내용, 보낼파일의경로, 보낼 메일 주소, 보내어질 파일 이름
                    new ConnectToInternet().execute(fileproperty); // 어싱크로 메일보내기


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //화면을 캡쳐해서 sdcard에 저장
    private void screenshot(Bitmap bm,String filename) {
        try {
            String str = Environment.getExternalStorageState();
            if (str.equals(Environment.MEDIA_MOUNTED)) {

                dirPath = "/sdcard/android/data/pe.berabue.sdtest/temp"; // 이미지를 저장할 경로
                File file = new File(dirPath);
                if (!file.exists())  // 원하는 경로에 폴더가 있는지 확인
                    file.mkdirs();

                dirpathAndFileName = "/"+dirPath+"/";
                dirpathAndFileName = dirpathAndFileName + filename;
                dirpathAndFileName = dirpathAndFileName + ".jpg"; // 이미지를 저장할 경로와 파일이름
                FileOutputStream out             = new FileOutputStream(dirpathAndFileName);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out); // 만듬

        } else {
            Toast.makeText(MainActivity.this, "SD Card 인식 실패", Toast.LENGTH_SHORT).show();
        }


    }

    catch(
    FileNotFoundException e
    )

    {
            Log.d("FileNotFoundException:", e.getMessage());
        }
    }


    //메일 보내는 곳
    private class ConnectToInternet extends AsyncTask<String,String,String[]> {

        @Override
        protected String[] doInBackground(String... data) {
            // TODO Auto-generated method stub

            sendEmail(data[0],data[1],data[2],data[3],data[4]);
            File file = new File(dirpathAndFileName); // 메모리를 관리할 목적으로 생성했던 파일은 다시 삭제
            if(file.exists()){
                file.delete();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
        }

    };

        private void sendEmail(String subject, String content, String path,String mailadd,String filename)
        {
            try
            {
                //gmail아이디와 password를 입력하는곳
                emailClient email = new emailClient("rupitere@gmail.com",
                        "비밀번호");
                email.sendMailWithFile(subject, content,
                        "rupitere@gmail.com", mailadd,
                        path, filename);
                // 제목, 내용, 발신자, 수신자, 현재 파일 위치, 보내게될 파일의 이름.
            } catch (Exception e)
            {
                Log.d("lastiverse", e.toString());
                Log.d("lastiverse", e.getMessage());
            } // try-catch
        } // void sendEmail

}















