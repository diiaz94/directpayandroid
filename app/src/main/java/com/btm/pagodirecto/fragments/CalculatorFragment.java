package com.btm.pagodirecto.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.btm.pagodirecto.R;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Formatter;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalculatorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalculatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalculatorFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public double finalResult = 0.0;
    public String[] oper = new String[3];

    @Bind(R.id.input_calculator)
    TextView inputResult;

    @Bind(R.id.btn_one)
    TextView btnOne;

    @Bind(R.id.btn_two)
    TextView btnTwo;

    @Bind(R.id.btn_three)
    TextView btnThree;

    @Bind(R.id.btn_four)
    TextView btnFour;

    @Bind(R.id.btn_five)
    TextView btnFive;

    @Bind(R.id.btn_six)
    TextView btnSix;

    @Bind(R.id.btn_seven)
    TextView btnSeven;

    @Bind(R.id.btn_eight)
    TextView btnEight;

    @Bind(R.id.btn_nine)
    TextView btnNine;

    @Bind(R.id.btn_zero)
    TextView btnZero;

    private OnFragmentInteractionListener mListener;

    public CalculatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalculatorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalculatorFragment newInstance(String param1, int param2) {
        CalculatorFragment fragment = new CalculatorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_calculator, container, false);
        ButterKnife.bind(this,v);

        // set a listener
        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        btnThree.setOnClickListener(this);
        btnFour.setOnClickListener(this);
        btnFive.setOnClickListener(this);
        btnSix.setOnClickListener(this);
        btnSeven.setOnClickListener(this);
        btnEight.setOnClickListener(this);
        btnNine.setOnClickListener(this);
        btnZero.setOnClickListener(this);

        this.clearOperators();
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onClick(View v){
        if(inputResult.getText().toString() == "0"){
            inputResult.setText("");
        }
        switch (v.getId()){
            case R.id.btn_one:
                inputResult.setText(inputResult.getText().toString()+"1");
                break;
            case R.id.btn_two:
                inputResult.setText(inputResult.getText().toString()+"2");
                break;
            case R.id.btn_three:
                inputResult.setText(inputResult.getText().toString()+"3");
                break;
            case R.id.btn_four:
                inputResult.setText(inputResult.getText().toString()+"4");
                break;
            case R.id.btn_five:
                inputResult.setText(inputResult.getText().toString()+"5");
                break;
            case R.id.btn_six:
                inputResult.setText(inputResult.getText().toString()+"6");
                break;
            case R.id.btn_seven:
                inputResult.setText(inputResult.getText().toString()+"7");
                break;
            case R.id.btn_eight:
                inputResult.setText(inputResult.getText().toString()+"8");
                break;
            case R.id.btn_nine:
                inputResult.setText(inputResult.getText().toString()+"9");
                break;
            case R.id.btn_zero:
                inputResult.setText(inputResult.getText().toString()+"0");
                break;
        }
        String result = inputResult.getText().toString();
        result.replace("Bs","");
        formatText(result,0,inputResult.getText().toString().length()-1,inputResult.getText().toString().length());
    }

    @OnClick(R.id.btn_delete)
    public void clearLastDigit(){
        if(inputResult.getText().toString().length() > 1 || !inputResult.getText().toString().isEmpty()){
            inputResult.setText(inputResult.getText().toString().substring(1, inputResult.length()-1));
            formatText(inputResult.getText().toString(),0,inputResult.getText().length()-1,inputResult.getText().length());
        }else{
            inputResult.setText("Bs.0,00");
        }
    }

    @OnClick(R.id.btn_c)
    public void clearAll(){
        this.clearOperators();
        inputResult.setText("Bs.0,00");
    }

    public void clearOperators(){
        this.oper[0] = "";
        this.oper[1] = "";
        this.oper[2] = "";
    }

    private String current = "";
    public void formatText(CharSequence s, int start, int before, int count) {
        if(!s.toString().equals(current)){
            String cleanString = s.toString().replaceAll("[Bs,.]", "");
            double parsed = Double.parseDouble(cleanString);
            String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));
            current = formatted;
            inputResult.setText(formatted);
        }
    }

    @OnClick(R.id.btn_plus)
    public void sum(){
        if (this.oper[0].isEmpty()){
            this.oper[0] = this.getTextWithoutFormat();
            this.oper[1] = "+"; // 10 +
            inputResult.setText("Bs.0,00");
        }else{
            this.oper[1] = "+";
            this.oper[2] = this.getTextWithoutFormat();
            inputResult.setText("Bs.0,00");
            this.doOperation(this.oper[0],this.oper[1],this.oper[2]); // 0 + 10
        }
    }

    @OnClick(R.id.btn_sustract)
    public void sustract(){
        if (this.oper[0].isEmpty()){
            this.oper[0] = inputResult.getText().toString();
            this.oper[1] = "-";
        }else{
            this.oper[1] = "-";
            this.oper[2] = this.getTextWithoutFormat();
            this.doOperation(this.oper[0],this.oper[1],this.oper[2]);
        }
    }

    @OnClick(R.id.btn_mult)
    public void mult(){
        this.oper[1] = "*";
        this.doOperation(this.oper[0],"*",inputResult.getText().toString());
    }

    @OnClick(R.id.btn_div)
    public void div(){
        this.oper[1] = "/";
        this.doOperation(this.oper[0],"/",inputResult.getText().toString());
    }

    @OnClick(R.id.btn_equal)
    public void equal(){
        this.oper[2] = this.getTextWithoutFormat();
        doOperation(this.oper[0],this.oper[1],this.oper[2]);
    }

    public void doOperation(String firstValue, String operator, String secondValue){ // 0 + 10
        Double result = 0.0;
        if (secondValue.isEmpty() || Double.parseDouble(secondValue) < 0) {
            result = Double.valueOf(this.finalResult);
        }else{
            switch (operator){
                case "+":
                    result = Double.parseDouble(firstValue) + Double.parseDouble(secondValue);
                break;
                case "-":
                    result = Double.parseDouble(firstValue) - Double.parseDouble(secondValue);
                break;
                case "*":
                    result = Double.parseDouble(firstValue) * Double.parseDouble(secondValue);
                break;
                case "/":
                    result = Double.parseDouble(firstValue) / Double.parseDouble(secondValue);
                break;
                default:
                    result = Double.valueOf(this.finalResult);
                    break;
            }
        }
        result = roundTwoDecimals(result);
        this.finalResult = result;
        this.oper[0] = result.toString();
        this.oper[1] = "";
        this.oper[2] = "";
        formatText(result.toString(),0,result.toString().length()-1,result.toString().length());
    }

    public String getTextWithoutFormat(){
        String text = inputResult.getText().toString();
        String cleanString = text.toString().replaceAll("[Bs.]","");
        cleanString = cleanString.toString().replaceAll("[,]",".");
        return  cleanString;
    }

    double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }
}

