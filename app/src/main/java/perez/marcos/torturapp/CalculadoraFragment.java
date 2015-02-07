package perez.marcos.torturapp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import extras.Notification;

/**
 * @author Carles Guivernau
 */
public class CalculadoraFragment extends Fragment implements View.OnClickListener {

    TextView tv;
    double value = 0;
    double mem = 0;
    double ans = 0;
    int op = 0;

    Bundle elPuto;

    boolean result = false;
    boolean toast = true;

    MenuItem iToast;
    MenuItem iNot;

    Button button0;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;
    Button button7;
    Button button8;
    Button button9;

    Button buttonmas;
    Button buttonmenos;
    Button buttonpor;
    Button buttondiv;
    Button buttonigual;

    Button buttonmem;
    Button buttonmemrec;
    Button buttonmemcle;

    Button punto;
    Button clear;
    Button answ;
    Button memClear;

    public static final String FILE = "archivo";


    /*
        * Oncreateview : rootView.findViewById
        *
        * */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calculadora, container, false);
        getActivity().setTitle(R.string.title_section1);


        tv = (TextView) rootView.findViewById(R.id.textView6);

        button0 = (Button) rootView.findViewById(R.id.button0);
        button1 = (Button) rootView.findViewById(R.id.button1);
        button2 = (Button) rootView.findViewById(R.id.button2);
        button3 = (Button) rootView.findViewById(R.id.button3);
        button4 = (Button) rootView.findViewById(R.id.button4);
        button5 = (Button) rootView.findViewById(R.id.button5);
        button6 = (Button) rootView.findViewById(R.id.button6);
        button7 = (Button) rootView.findViewById(R.id.button7);
        button8 = (Button) rootView.findViewById(R.id.button8);
        button9 = (Button) rootView.findViewById(R.id.button9);
        buttonmas = (Button) rootView.findViewById(R.id.buttonmas);
        buttonmenos = (Button) rootView.findViewById(R.id.buttonmenos);
        buttonpor = (Button) rootView.findViewById(R.id.buttonpor);
        buttondiv = (Button) rootView.findViewById(R.id.buttondiv);
        buttonigual = (Button) rootView.findViewById(R.id.buttonigual);

        buttonmem = (Button) rootView.findViewById(R.id.buttonmem);
        buttonmemrec = (Button) rootView.findViewById(R.id.buttonmemrec);
        buttonmemcle = (Button) rootView.findViewById(R.id.buttonmemcle);

        punto = (Button) rootView.findViewById(R.id.buttonpunto);
        clear = (Button) rootView.findViewById(R.id.buttonclear);
        answ = (Button) rootView.findViewById(R.id.buttonans);
        memClear = (Button) rootView.findViewById(R.id.buttonclear);

        //hacerlo de manera recursiva
        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttonmas.setOnClickListener(this);
        buttonmenos.setOnClickListener(this);
        buttondiv.setOnClickListener(this);
        buttonpor.setOnClickListener(this);

        buttonmemrec.setOnClickListener(this);
        buttonmemcle.setOnClickListener(this);

        punto.setOnClickListener(this);
        clear.setOnClickListener(this);
        buttonmem.setOnClickListener(this);
        answ.setOnClickListener(this);
        memClear.setOnClickListener(this);
        buttonigual.setOnClickListener(this);

        SharedPreferences sp = this.getActivity().getSharedPreferences(FILE, Context.MODE_PRIVATE);
        value = Double.parseDouble(sp.getString("val", "0"));
        mem = Double.parseDouble(sp.getString("mem", "0"));
        ans = Double.parseDouble(sp.getString("ans", "0"));
        result = sp.getBoolean("res", false);
        toast = sp.getBoolean("toast", true);
        op = sp.getInt("op", 0);
        tv.setText(sp.getString("result", "0"));
        if (result) tv.setTextColor(Color.RED);
        setHasOptionsMenu(true);

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_calc, menu);
    }

    public void activate_toast() {
        toast = true;
        iToast.setChecked(true);
        iNot.setChecked(false);
    }

    public void activate_notif() {
        toast = false;
        iNot.setChecked(true);
        iToast.setChecked(false);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        iToast = menu.findItem(R.id.toast);
        iNot = menu.findItem(R.id.not);

        if (toast) activate_toast();
        else activate_notif();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.call:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tv.getText()));
                startActivity(intent);
                break;
            case R.id.browser:
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://tutazadecafe.com"));
                startActivity(i);
                break;
            case R.id.toast:
                if (!toast) {
                    activate_toast();
                }
                break;
            case R.id.not:
                if (toast) {
                    activate_notif();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences sp = this.getActivity().getSharedPreferences(FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("val", value + "");
        editor.putString("mem", mem + "");
        editor.putInt("op", op);
        editor.putString("ans", ans + "");
        editor.putBoolean("res", result);
        editor.putBoolean("toast", toast);
        editor.putString("result", tv.getText().toString());
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        Button press = (Button) v;
        String s = press.getText().toString();
        String curr = tv.getText().toString(); //tot el text
        if (!result) tv.setTextColor(-7829368);
        switch (s) { //lo que acabamos de pulsar
            case "=":
                if (!esNum(curr)) {
                    tv.setText(String.valueOf(value));
                } else {
                    calcula(curr);
                    tv.setTextColor(Color.RED); //rojo
                    tv.setText(String.valueOf(value));
                    result = true;
                }
                break;
            case "+":
                if (esNum(curr)) value = Double.parseDouble(curr);
                tv.setTextColor(-7829368);
                tv.setText(s);
                op = 1;
                break;
            case "-":
                if (esNum(curr)) value = Double.parseDouble(curr);
                tv.setTextColor(-7829368);
                tv.setText(s);
                op = 2;
                break;
            case "*":
                if (esNum(curr)) value = Double.parseDouble(curr);
                tv.setTextColor(-7829368);
                tv.setText(s);
                op = 3;
                break;
            case "/":
                if (esNum(curr)) value = Double.parseDouble(curr);
                tv.setTextColor(-7829368);
                tv.setText(s);
                op = 4;
                break;
            case "M":
                if (esNum(curr)) mem = Double.parseDouble(curr);
                break;
            case "MC":
                if (esNum(curr)) mem = Double.parseDouble(curr);
                mem = 0;
                break;
            case "MR":
                tv.setText(mem + "");
                break;
            case "ANS":
                if (result) {
                    result = false;
                    tv.setTextColor(-7829368);
                }
                tv.setText(ans + "");
                break;
            case ".":
                if (!result) {
                    if (tv.getText().toString().contains(".")) {
                        tv.setText(tv.getText().toString().replace(".", ""));
                    }
                    tv.setText(tv.getText() + ".");
                }
                break;
            case "CE":
                if (!result) {
                    String x = tv.getText().toString();
                    CharSequence y = x.subSequence(0, x.length() - 1);
                    if (y.toString().isEmpty()) y = "0";
                    tv.setText(y);
                }
                break;
            default: //hemos pulsado un numero
                double val = Double.parseDouble(s);
                switch (curr) { //lo que tenemos escrito
                    case "0":
                        tv.setText(s);
                        break;
                    default: //es simbolo o numero largo
                        if (esNum(curr) && !result) {
                            tv.setText(tv.getText() + s);
                        } else { //es simbolo o resultado
                            tv.setTextColor(-7829368);
                            tv.setText(s);
                            result = false;
                        }
                        break;
                }
                break;
        }
    }

    private void calcula(String s) {
        switch (op) {
            case 1:
                value = value + Double.parseDouble(s);
                break;
            case 2:
                value = value - Double.parseDouble(s);
                break;
            case 3:
                value = value * Double.parseDouble(s);
                break;
            case 4:
                if (s.equals("0")) {
                    if (toast) {
                        Toast.makeText(getActivity(), "ERROR: División por 0", Toast.LENGTH_SHORT).show();
                    } else {
                        Notification not = new Notification();
                        not.setContext(getActivity());
                        not.setResume("División por 0");
                        not.setText("No se puede dividir por 0");
                        not.setTitle("Error");
                        not.build();
                    }
                } else {
                    value = value / Double.parseDouble(s);
                }
                break;
        }
        ans = value;
    }

    private boolean esNum(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }


}
