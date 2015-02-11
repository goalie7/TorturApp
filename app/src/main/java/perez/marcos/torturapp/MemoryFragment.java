package perez.marcos.torturapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import extras.MyDialogList;
import extras.MyDialogWin;
import extras.UserHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MemoryFragment extends Fragment implements View.OnClickListener, MyDialogWin.win {
    UserHelper uh;
    boolean select;
    Integer intentos;
    Integer lastimg;
    ImageView lastview;
    ImageView aux;
    Integer pairs;
    boolean pause;
    TextView inte;
    ImageView c00;
    ImageView c01;
    ImageView c02;
    ImageView c03;
    ImageView c10;
    ImageView c11;
    ImageView c12;
    ImageView c13;
    ImageView c20;
    ImageView c21;
    ImageView c22;
    ImageView c23;
    ImageView c30;
    ImageView c31;
    ImageView c32;
    ImageView c33;
    ImageView casper;
    private HashMap<ImageView,Integer> relations;
    private ArrayList<Integer> imgs; //imagenes existentes
    private ArrayList<ImageView> cards; //cada una de las imgview
    private MyDialogWin MDW;
    private Integer resID;
    private String front = "front";
    private String back = "back";
    SharedPreferences sp;
    String logged;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_mem, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.res:
                Reset();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout2, container, false);
        getActivity().setTitle(R.string.title_section2);
        SharedPreferences sp = this.getActivity().getSharedPreferences("logged", Context.MODE_PRIVATE);
        logged = sp.getString("logged",null);
        uh = new UserHelper(getActivity().getApplicationContext());
        inte = (TextView) rootView.findViewById(R.id.textView13);
        c00 = (ImageView) rootView.findViewById(R.id.c00);
        c01 = (ImageView) rootView.findViewById(R.id.c01);
        c02 = (ImageView) rootView.findViewById(R.id.c02);
        c03 = (ImageView) rootView.findViewById(R.id.c03);
        c10 = (ImageView) rootView.findViewById(R.id.c10);
        c11 = (ImageView) rootView.findViewById(R.id.c11);
        c12 = (ImageView) rootView.findViewById(R.id.c12);
        c13 = (ImageView) rootView.findViewById(R.id.c13);
        c20 = (ImageView) rootView.findViewById(R.id.c20);
        c21 = (ImageView) rootView.findViewById(R.id.c21);
        c22 = (ImageView) rootView.findViewById(R.id.c22);
        c23 = (ImageView) rootView.findViewById(R.id.c23);
        c30 = (ImageView) rootView.findViewById(R.id.c30);
        c31 = (ImageView) rootView.findViewById(R.id.c31);
        c32 = (ImageView) rootView.findViewById(R.id.c32);
        c33 = (ImageView) rootView.findViewById(R.id.c33);
        casper = new ImageView(getActivity());
        casper.setImageDrawable(getResources().getDrawable(R.drawable.backcard));
        c00.setOnClickListener(this);
        c01.setOnClickListener(this);
        c02.setOnClickListener(this);
        c03.setOnClickListener(this);
        c10.setOnClickListener(this);
        c11.setOnClickListener(this);
        c12.setOnClickListener(this);
        c13.setOnClickListener(this);
        c20.setOnClickListener(this);
        c21.setOnClickListener(this);
        c22.setOnClickListener(this);
        c23.setOnClickListener(this);
        c30.setOnClickListener(this);
        c31.setOnClickListener(this);
        c32.setOnClickListener(this);
        c33.setOnClickListener(this);
        Reset();
        setHasOptionsMenu(true);


        return rootView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Activity a = getActivity();
            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onClick(View v) {
        Integer x = relations.get(v);
        aux = (ImageView) v;
         if (!pause && (!aux.getTag().equals(front))){
            if (!select) {
                aux.setImageResource(x);
                aux.setTag(front);
                select = true;
                lastimg = x;
                lastview = aux;
            } else {
                aux.setImageResource(x);
                aux.setTag(front);
                if (x == lastimg && lastview != aux) {
                    ++pairs;
                    lastimg = null;
                    select = false;
                    if (pairs == 8){
                        //guardar en BD si intentos < que lo que haya
                        FragmentManager fm = getFragmentManager();
                        MDW = new MyDialogWin();
                        MDW.setTargetFragment(this,0);
                        uh.updateRecord(logged, intentos);
                        MDW.show(fm, "tag");
                    }
                } else {
                    ++intentos;
                    lastimg = null;
                    select = false;
                    inte.setText(String.valueOf(intentos));
                    pause = true;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            aux.setImageResource(getResources().getIdentifier("perez.marcos.torturapp:drawable/backcard", null, null));
                            aux.setTag(back);
                            lastview.setImageResource(getResources().getIdentifier("perez.marcos.torturapp:drawable/backcard", null, null));
                            lastview.setTag(back);
                            pause = false;
                        }
                    }, 2000);
                }
            }
        }
    }

    public void Reset(){
        select = false;
        intentos = 0;
        pairs = 0;
        lastimg = null;
        pause = false;
        inte.setText("0");
        new CreateMemory().execute();
        new CreateRelations().execute();
    }

    @Override
    public void win() {
        Reset();
    }

    private class CreateMemory extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            resID = getResources().getIdentifier("perez.marcos.torturapp:drawable/backcard", null, null);
            c00.setImageResource(resID);
            c01.setImageResource(resID);
            c02.setImageResource(resID);
            c03.setImageResource(resID);

            c10.setImageResource(resID);
            c11.setImageResource(resID);
            c12.setImageResource(resID);
            c13.setImageResource(resID);

            c20.setImageResource(resID);
            c21.setImageResource(resID);
            c22.setImageResource(resID);
            c23.setImageResource(resID);

            c30.setImageResource(resID);
            c31.setImageResource(resID);
            c32.setImageResource(resID);
            c33.setImageResource(resID);
        }
        @Override
        protected Void doInBackground(Void... params) {
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            c00.setTag(back);
            c01.setTag(back);
            c02.setTag(back);
            c03.setTag(back);

            c10.setTag(back);
            c11.setTag(back);
            c12.setTag(back);
            c13.setTag(back);

            c20.setTag(back);
            c21.setTag(back);
            c22.setTag(back);
            c23.setTag(back);

            c30.setTag(back);
            c31.setTag(back);
            c32.setTag(back);
            c33.setTag(back);

            cards = new ArrayList<ImageView>();
            cards.add(c01);
            cards.add(c00);
            cards.add(c02);
            cards.add(c03);
            cards.add(c10);
            cards.add(c11);
            cards.add(c12);
            cards.add(c13);
            cards.add(c20);
            cards.add(c21);
            cards.add(c22);
            cards.add(c23);
            cards.add(c30);
            cards.add(c31);
            cards.add(c32);
            cards.add(c33);

            imgs = new ArrayList<Integer>();
            imgs.add(R.drawable.ascorazones);
            imgs.add(R.drawable.asrombos);
            imgs.add(R.drawable.aspicas);
            imgs.add(R.drawable.astrebol);
            imgs.add(R.drawable.ascopas);
            imgs.add(R.drawable.asbastos);
            imgs.add(R.drawable.asespadas);
            imgs.add(R.drawable.asoros);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //this method will be running on UI thread
        }

    }


    private class CreateRelations extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread

        }
        @Override
        protected Void doInBackground(Void... params) {
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            relations = new HashMap<ImageView,Integer>();
            Random randomGenerator = new Random();
            while(imgs.iterator().hasNext()){
                int j = 1;
                while(j >= 0){
                    int randomInt = randomGenerator.nextInt(cards.size());
                    relations.put(cards.get(randomInt),imgs.iterator().next());
                    cards.remove(randomInt);
                    --j;
                }
                imgs.remove(imgs.iterator().next());
            }
            return null;
        }



        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //this method will be running on UI thread
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            Reset();
        }
    }
}
