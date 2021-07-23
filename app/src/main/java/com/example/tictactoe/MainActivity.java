package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_VERSUS_AI = "KEY_VERSUS_AI";
    private static final String KEY_MAP="KEY_MAP";
    private static final String KEY_GAME="KEY_GAME";

    private final int AREA_SIZE=7;
    private final int MARKFORWIN=5;
    private boolean VERSUS_AI;
    private Map map;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        VERSUS_AI=getIntent().getBooleanExtra(KEY_VERSUS_AI,false);

        if (savedInstanceState==null) {
            map = new Map(AREA_SIZE);
            game = new Game(VERSUS_AI, map, MARKFORWIN);
        } else {
            map = (Map) savedInstanceState.getSerializable(KEY_MAP);
            game = (Game) savedInstanceState.getSerializable(KEY_GAME);
        }

        showArea();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_restart) {
            restartGame();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(KEY_MAP,map);
        outState.putSerializable(KEY_GAME,game);
    }

    private void restartGame() {
        map.clearMap();
        game = new Game(VERSUS_AI, map, MARKFORWIN);
        showArea();
    }

    private void showArea() {
        TableLayout area = findViewById(R.id.game_area);

        area.removeAllViews();

        for (int i=0; i<AREA_SIZE; i++){
            TableRow row = new TableRow(this);

            TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,1);
            row.setLayoutParams(rowParams);

            for (int j = 0; j < AREA_SIZE; j++) {
                Button btn = new Button(this);

                TableRow.LayoutParams params = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
                btn.setLayoutParams(params);

                btn.setText(String.valueOf(map.getDot(i,j)));
                btn.setTextSize(20);

                final int fi=i;
                final int fj=j;

                btn.setOnClickListener(v -> btnDotClick(fi, fj, (Button) v));

                row.addView(btn);
            }

            area.addView(row);
        }
    }

    private void btnDotClick(int fi, int fj, Button v) {
        if (game.isEnd()){
            return;
        }

        char curDot = game.getCurDot();
        if (game.playerTurn(fi,fj)){
            v.setText(String.valueOf(curDot));

            if (game.isEnd()){
                showWinner();
                return;
            }

            if (VERSUS_AI) {
                game.aiTurn();
                showArea();

                if (game.isEnd()) {
                    showWinner();
                    return;
                }
            }
        }
    }

    private void showWinner() {
        int winner = game.getWinnerPlayerNum();
        if (winner==-1){
            showMsg("НИЧЬЯ");
        }else{
            if (VERSUS_AI) {
                if (winner==1) {
                    showMsg("УРА! ВЫ ПОБЕДИЛИ!");
                } else {
                    showMsg("ВЫ ПРОИГРАЛИ КОМПЬЮТЕРУ!");
                }
            }
            else {
                showMsg("ИГРОК "+winner+" ПОБЕДИЛ");
            }
        }
    }

    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}