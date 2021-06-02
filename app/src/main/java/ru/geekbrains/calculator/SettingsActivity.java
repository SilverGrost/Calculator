package ru.geekbrains.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import ru.geekbrains.calculator.ui.Constants;
import ru.geekbrains.calculator.ui.MainActivity;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, Constants {

    private int radioBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // получить данные из Intent
        radioBtn = getIntent().getExtras().getInt(intentParam);
        setTheme(MainActivity.rbToStyleID(radioBtn));
        setContentView(R.layout.activity_settings);
        initView(radioBtn);
    }

    // Инициализируем все вьюхи
    private void initView(int rb) {
        RadioButton radioButtonMyStyle = findViewById(R.id.radioButton6);
        RadioButton radioButtonLightStyle = findViewById(R.id.radioButton7);
        RadioButton radioButtonDarkStyle = findViewById(R.id.radioButton8);
        Button buttonFinish = findViewById(R.id.button_finish);

        radioButtonMyStyle.setOnClickListener(this);
        radioButtonLightStyle.setOnClickListener(this);
        radioButtonDarkStyle.setOnClickListener(this);
        buttonFinish.setOnClickListener(this);

        switch (rb) {
            case 0:
                radioButtonMyStyle.setChecked(true);
                break;
            case 1:
                radioButtonLightStyle.setChecked(true);
                break;
            default:
                radioButtonDarkStyle.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.radioButton6) {//applyTheme(R.style.AppThemeMy);
            radioBtn = AppThemeMy;
        } else if (id == R.id.radioButton7) {//applyTheme(R.style.AppThemeLight);
            radioBtn = AppThemeLight;
        } else if (id == R.id.radioButton8) {//applyTheme(R.style.AppThemeDark);
            radioBtn = AppThemeDark;
        } else if (id == R.id.button_finish) {
            Intent intentResult = new Intent();
            intentResult.putExtra(intentParam, radioBtn);
            setResult(RESULT_OK, intentResult);

            finish();
        }
    }
}