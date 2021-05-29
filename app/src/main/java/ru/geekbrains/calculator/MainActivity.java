package ru.geekbrains.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private boolean isEnterPress = false;
    private TextView textView_Input;
    private TextView textView_Result;
    private TextView textView_History;

    private String arguments = "";

    private boolean canDot = true;


    private void initView() {
        Button button_0 = findViewById(R.id.button_0);
        Button button_1 = findViewById(R.id.button_1);
        Button button_2 = findViewById(R.id.button_2);
        Button button_3 = findViewById(R.id.button_3);
        Button button_4 = findViewById(R.id.button_4);
        Button button_5 = findViewById(R.id.button_5);
        Button button_6 = findViewById(R.id.button_6);
        Button button_7 = findViewById(R.id.button_7);
        Button button_8 = findViewById(R.id.button_8);
        Button button_9 = findViewById(R.id.button_9);

        Button button_dot = findViewById(R.id.button_dot);

        Button button_perc = findViewById(R.id.button_perc);

        Button button_div = findViewById(R.id.button_div);
        Button button_multi = findViewById(R.id.button_multi);
        Button button_sub = findViewById(R.id.button_sub);
        Button button_add = findViewById(R.id.button_add);

        Button button_enter = findViewById(R.id.button_enter);

        Button button_cl = findViewById(R.id.button_cl);

        Button button_bs = findViewById(R.id.button_bs);

        Button button_bracketOpen = findViewById(R.id.button_bracketOpen);
        Button button_bracketClose = findViewById(R.id.button_bracketClose);
        Button button_exponent = findViewById(R.id.button_exponent);
        Button button_sqrt = findViewById(R.id.button_sqrt);
        Button button_pi = findViewById(R.id.button_pi);

        button_0.setOnClickListener(this);
        button_1.setOnClickListener(this);
        button_2.setOnClickListener(this);
        button_3.setOnClickListener(this);
        button_4.setOnClickListener(this);
        button_5.setOnClickListener(this);
        button_6.setOnClickListener(this);
        button_7.setOnClickListener(this);
        button_8.setOnClickListener(this);
        button_9.setOnClickListener(this);

        button_dot.setOnClickListener(this);

        button_perc.setOnClickListener(this);

        button_div.setOnClickListener(this);
        button_multi.setOnClickListener(this);
        button_sub.setOnClickListener(this);
        button_add.setOnClickListener(this);

        button_enter.setOnClickListener(this);

        button_cl.setOnClickListener(this);

        button_bs.setOnClickListener(this);

        button_bracketOpen.setOnClickListener(this);
        button_bracketClose.setOnClickListener(this);
        button_exponent.setOnClickListener(this);
        button_sqrt.setOnClickListener(this);
        button_pi.setOnClickListener(this);

        textView_Input = findViewById(R.id.textView_Operation);
        textView_Result = findViewById(R.id.textView_Result);
        textView_History = findViewById(R.id.textView_History);
    }

    public void clear(){
        textView_Input.setText("");
        textView_Result.setText("");
        arguments = "";
         canDot = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    public void onClick(View v) {
        String operations = "^%/*-+C⇐";
        String dotCan = "%/*-+";
        String enterFalse = "^%C⇐";

        String inText = (String) ((Button) findViewById(v.getId())).getText();

        if (!enterFalse.contains(inText))
            isEnterPress = false;

        if (!operations.contains(inText)){
            if (isEnterPress){
                clear();
                isEnterPress = false;
            }
        }

        if (inText.equals("*")) {
            if (canDot) {
                canDot = false;
            }
        }

        if (dotCan.contains(inText))
            canDot = false;

        if (inText.equals("C"))
            clear();

        if (inText.equals("="))
            isEnterPress = true;

        CalcTextData input = new CalcTextData((String)textView_History.getText(), (String)textView_Input.getText(), (String)textView_Result.getText(), arguments);
        CalcTextData result;

        result = Calculate.procInput(input, inText);

        textView_Input.setText(result.getTvInput());
        textView_Result.setText(result.getTvResult());
        textView_History.setText(result.getTvHistory());
        arguments = result.getArguments();

    }
}