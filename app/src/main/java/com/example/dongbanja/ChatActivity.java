
package com.example.dongbanja;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//액티비티 시작시 오픈된 채팅방을 검색하여
//자신의 아이디가 들어간 채팅방과 채팅방의 하위 객체인 uid2로 되어있는 자신의 채팅방을 확인
//chatActivity 연결

public class ChatActivity extends AppCompatActivity {



    private ListView chat_view;
    private EditText chat_edit;
    private Button chat_send;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseAuth firebaseAuth;
    List<String> roomKeyList = new ArrayList<>();
    String roomKey ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        firebaseAuth = FirebaseAuth.getInstance();// FirebaseAuth 인스턴스 생성
        // 채팅방 키, 유저 이름 저장

        final String userId = firebaseAuth.getUid();
        final String chatUser2="";
        chat_view = (ListView) findViewById(R.id.chat_view);
        chat_edit = (EditText) findViewById(R.id.chat_edit);
        chat_send = (Button) findViewById(R.id.chat_sent);

        // 로그인 화면에서 받아온 채팅방 이름, 유저 이름 저장
        Intent intent = getIntent();

        databaseReference = FirebaseDatabase.getInstance().getReference("chat_room").child("room").child("uid");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String s ="";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    s = snapshot.getValue().toString();
                    if(s.contains(userId)){

                        roomKey = snapshot.getKey();
                        // 채팅 방 입장
                        databaseReference = FirebaseDatabase.getInstance().getReference("chat_room").child("room");
                        openChat(roomKey);

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        // 메시지 전송 버튼에 대한 클릭 리스너 지정
        chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chat_edit.getText().toString().equals(""))
                    return;

                ChatDTO chat = new ChatDTO( chat_edit.getText().toString()); //ChatDTO를 이용하여 데이터를 묶는다.
                databaseReference.child(roomKey).push().setValue(chat); // 데이터를 json파일에 chat의 자식으로 넣는다
                chat_edit.setText(""); //입력창 초기화


            }
        });
    }

    private void addMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
        adapter.add( chatDTO.getMessage());
    }

  /*  private void removeMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
        adapter.remove(chatDTO.getUserName() + " : " + chatDTO.getMessage());
    }*/

    private void openChat(String user1) {
        // 리스트 어댑터 생성 및 세팅
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        chat_view.setAdapter(adapter);

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child(user1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addMessage(dataSnapshot, adapter);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //  removeMessage(dataSnapshot, adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



}