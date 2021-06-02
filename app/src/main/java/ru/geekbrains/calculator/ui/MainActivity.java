package ru.geekbrains.calculator.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import ru.geekbrains.calculator.SettingsActivity;
import ru.geekbrains.calculator.calc.CalcTextData;
import ru.geekbrains.calculator.calc.Calculate;
import ru.geekbrains.calculator.R;

import static ru.geekbrains.calculator.ui.Constants.AppTheme;
import static ru.geekbrains.calculator.ui.Constants.AppThemeDark;
import static ru.geekbrains.calculator.ui.Constants.AppThemeLight;
import static ru.geekbrains.calculator.ui.Constants.AppThemeMy;
import static ru.geekbrains.calculator.ui.Constants.NameSharedPreference;
import static ru.geekbrains.calculator.ui.Constants.REQUEST_CODE_SETTING_ACTIVITY;
import static ru.geekbrains.calculator.ui.Constants.dateForSave;
import static ru.geekbrains.calculator.ui.Constants.intentParam;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int codeStyle;
    private boolean isEnterPress = false;
    private TextView textView_Input;
    private TextView textView_Result;
    private TextView textView_History;
    private String arguments = "";
    private boolean canDot = true;
    private CalcTextData calcTextData;
    private final int[] numberButtonIds = new int[]{R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3,
            R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9};
    private ConstraintLayout constraintLayout;
    private static int AppThemeDefault = AppThemeMy;

    private void setNumberButtonListeners() {
        for (int numberButtonId : numberButtonIds) {
            findViewById(numberButtonId).setOnClickListener(this);
        }
    }

    //Инициализируем все вьюхи
    private void initView() {
        setNumberButtonListeners();

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
        Button button_settings = findViewById(R.id.button_settings);

        constraintLayout = findViewById(R.id.consL);

        textView_Input = findViewById(R.id.textView_Operation);
        textView_Result = findViewById(R.id.textView_Result);
        textView_History = findViewById(R.id.textView_History);

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

        button_settings.setOnClickListener(this);
    }

    public void clear() {
        textView_Input.setText("");
        textView_Result.setText("");
        arguments = "";
        canDot = true;
    }

    // Чтение настроек, параметр «тема»
    protected int loadTheme(int codeStyle) {
        // Работаем через специальный класс сохранения и чтения настроек
        SharedPreferences sharedPref = getSharedPreferences(NameSharedPreference, MODE_PRIVATE);
        //Прочитать тему, если настройка не найдена - взять по умолчанию
        int result = sharedPref.getInt(AppTheme, codeStyle);
        AppThemeDefault = result;
        return result;
    }

    // Сохранение настроек
    protected void saveTheme(int codeStyle) {
        SharedPreferences sharedPref = getSharedPreferences(NameSharedPreference, MODE_PRIVATE);
        // Настройки сохраняются посредством специального класса editor.
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(AppTheme, codeStyle);
        editor.apply();
    }

    // Преобразуем id ресурса темы в код стиля
    private int codeStyleThemeToStyleId(int idStyle) {
        if (idStyle == R.style.AppThemeLight) {
            return AppThemeLight;
        } else if (idStyle == R.style.AppThemeDark) {
            return AppThemeDark;
        }
        return AppThemeMy;
    }

    public static int rbToStyleID(int rb_loc) {
        switch (rb_loc) {
            case 1:
                return R.style.AppThemeLight;
            case 2:
                return R.style.AppThemeDark;
            default:
                return R.style.AppThemeMy;
        }
    }

    // Применяем BackGround
    private void setBGtoLayout(int codeStyle) {
        switch (codeStyle) {
            case 0:
                constraintLayout.setBackgroundResource(R.drawable.handcalc_original);
                break;
            case 1:
                constraintLayout.setBackgroundResource(R.drawable.handcalc);
                break;
            default:
                constraintLayout.setBackgroundResource(R.drawable.handcalc_dark);
                break;
        }
    }

    // Применяем тему
    private void applyTheme(int codeStyle) {
        saveTheme(codeStyle);
        setTheme(codeStyle);
        recreate();
        setBGtoLayout(codeStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppThemeDefault = loadTheme(AppThemeDefault);
        codeStyle = codeStyleThemeToStyleId(AppThemeDefault);
        setTheme(AppThemeDefault);
        setContentView(R.layout.activity_main);
        initView();
        super.onCreate(savedInstanceState);
        setBGtoLayout(codeStyle);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE_SETTING_ACTIVITY) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (resultCode == RESULT_OK) {
            codeStyle = (int) data.getIntExtra(intentParam, AppThemeDefault);
            AppThemeDefault = rbToStyleID(codeStyle);
            applyTheme(AppThemeDefault);
        }
    }

    @Override
    public void onClick(View v) {
        String operations = "%^/*-+C⇐";
        String dotCan = "%/*-+";
        String enterFalse = "^%C⇐";
        String digits = "0123456789";
        String inText;

        if (v.getId() == R.id.button_settings) {
            // Чтобы стартовать активити, надо подготовить интент
            // В данном случае это будет явный интент, поскольку здесь передаётся класс активити
            Intent runSettings = new Intent(MainActivity.this, SettingsActivity.class);
            // Передача данных через интент
            runSettings.putExtra(intentParam, codeStyle);
            // Метод стартует активити, указанную в интенте
            //startActivity(runSettings);
            startActivityForResult(runSettings, REQUEST_CODE_SETTING_ACTIVITY);
        } else {
            // Получим текст из кнопки
            inText = (String) ((MaterialButton) findViewById(v.getId())).getText();

            // Обработка цифровых кнопок
            if (digits.contains(inText)) {
                if (isEnterPress) {
                    clear();
                    isEnterPress = false;
                }
            }

            // Обработка кнопок, после которых не надо сбрасывать флаг нажатия на "="
            if (!enterFalse.contains(inText))
                isEnterPress = false;

            // Обработка операционных кнопок
            if (!operations.contains(inText)) {
                if (isEnterPress) {
                    clear();
                    isEnterPress = false;
                }
            }

            // Обработка Точки, что бы не было несколько
            if (inText.equals(".")) {
                if (canDot) {
                    canDot = false;
                }
            }

            // Обработка Точки
            if (dotCan.contains(inText))
                canDot = false;

            // Обработка Clear
            if (inText.equals("C"))
                clear();

            // Обработка "="
            if (inText.equals("="))
                isEnterPress = true;

            // Запишем данные всех трёх TextView в Parcelable класс
            CalcTextData input = new CalcTextData((String) textView_History.getText(), (String) textView_Input.getText(), (String) textView_Result.getText(), arguments);

            // Создадим пустой экземпляр для результата вычислений
            CalcTextData result;

            // Полчаем результат
            result = Calculate.procInput(input, inText);

            //Вытаскиваем из результата данные для всех TextView
            textView_Input.setText(result.getTvInput());
            textView_Result.setText(result.getTvResult());
            textView_History.setText(result.getTvHistory());
            arguments = result.getArguments();
        }
    }


    // Сохранение данных
    @Override
    public void onSaveInstanceState(@NonNull Bundle instanceState) {
        calcTextData = new CalcTextData(textView_History.getText().toString(), textView_Input.getText().toString(), textView_Result.getText().toString(), arguments);
        super.onSaveInstanceState(instanceState);
        instanceState.putParcelable(dateForSave, calcTextData);
    }

    // Восстановление данных
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle instanceState) {
        super.onRestoreInstanceState(instanceState);
        calcTextData = instanceState.getParcelable(dateForSave);
        setDataFromSave();
    }

    private void setTextToTV(TextView tv, String data) {
        tv.setText(data);
    }

    private void setDataFromSave() {
        setTextToTV(textView_Input, calcTextData.getTvInput());
        setTextToTV(textView_Result, calcTextData.getTvResult());
        setTextToTV(textView_History, calcTextData.getTvHistory());
        arguments = calcTextData.getArguments();
    }
}