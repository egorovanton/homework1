package ru.ifmo.android_2016.calc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public final class CalculatorActivity extends Activity implements View.OnClickListener {
    private String enteredText = "";
    private TextView resultView;

    private String KEY_SAVED_INSTANCE_ENTERED_TEXT = "KEY_SAVED_INSTANCE_ENTERED_TEXT";
    private String KEY_SAVED_INSTANCE_DISPLAYED_TEXT = "KEY_SAVED_INSTANCE_DISPLAYED_TEXT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calculator);

        findViewById(R.id.d0).setOnClickListener(this);
        findViewById(R.id.d1).setOnClickListener(this);
        findViewById(R.id.d2).setOnClickListener(this);
        findViewById(R.id.d3).setOnClickListener(this);
        findViewById(R.id.d4).setOnClickListener(this);
        findViewById(R.id.d5).setOnClickListener(this);
        findViewById(R.id.d6).setOnClickListener(this);
        findViewById(R.id.d7).setOnClickListener(this);
        findViewById(R.id.d8).setOnClickListener(this);
        findViewById(R.id.d9).setOnClickListener(this);
        findViewById(R.id.add).setOnClickListener(this);
        findViewById(R.id.sub).setOnClickListener(this);
        findViewById(R.id.mul).setOnClickListener(this);
        findViewById(R.id.div).setOnClickListener(this);
        findViewById(R.id.dot).setOnClickListener(this);
        findViewById(R.id.open_bracket).setOnClickListener(this);
        findViewById(R.id.close_bracket).setOnClickListener(this);

        resultView = (TextView) findViewById(R.id.result);

        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredText = "";
                updateText("");
            }
        });

        findViewById(R.id.eqv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    updateText(String.valueOf(Calc.calculate(enteredText)));
                } catch (Exception e) {
                    e.printStackTrace();
                    updateText("PARSE ERROR");
                } finally {
                    enteredText = "";
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(KEY_SAVED_INSTANCE_DISPLAYED_TEXT, resultView.getText());
        outState.putString(KEY_SAVED_INSTANCE_ENTERED_TEXT, enteredText);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        resultView.setText(savedInstanceState.getCharSequence(KEY_SAVED_INSTANCE_DISPLAYED_TEXT, ""));
        enteredText = savedInstanceState.getString(KEY_SAVED_INSTANCE_ENTERED_TEXT, "");
    }

    @Override
    public void onClick(View view) {
        enteredText += ((Button) view).getText();
        updateText(enteredText);
    }

    private void updateText(String newText) {
        resultView.setText(newText);
    }
}
