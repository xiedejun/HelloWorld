package com.tcl.example.com.tcl.dejun.xie.ReadAndWriteData;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tcl.example.com.tcl.dejun.xie.Utils.ToastUtils;
import com.tcl.example.com.tcl.dejun.xie.startActivityForResult.Strategy;
import com.tcl.example.xie.dejun.diyview.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.xml.datatype.Duration;

/**
 * Created by dejun.xie on 2016/9/2.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    //设置给“X”（清空输入）图片的tag常量
    public static final String ACCOUNT_CLEAR="ACCOUNT_CLEAR";
    public static final String PASSWORD_CLEAR="PASSWORD_CLEAR";
    public static final int MAX_LENGTH = 10;

    private LinearLayout llAccount;
    private LinearLayout llPassword;
    private EditText etAccount;
    private TextView tvPassword;
    private EditText etPassword;
    private ImageView ivNoHide;
    private boolean showPassword=false;
    private ImageView accountClear;
    private ImageView passwordClear;
    private Context mContext;
    private LinearLayout llRemPassword;
    private LinearLayout llAutoLogin;
    private TextView tvRemPassword;
    private TextView tvAutoLogin;
    private CheckBox cbRemPassword;
    private CheckBox cbAutoLogin;
    private Button btLogin;
    private Button btRegister;
    private String strPassword;
    //    private ArrayList<String> accountList;

    //文件名
    private final String fileName="user_info.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        findViews();
        initViews();
        initListeners();
    }

    private void findViews() {
        //账号布局
        llAccount = (LinearLayout) findViewById(R.id.layout_account);
        etAccount = (EditText) llAccount.findViewById(R.id.et_account);
        //密码布局
        llPassword = (LinearLayout) findViewById(R.id.layout_password);
        tvPassword = (TextView) llPassword.findViewById(R.id.tv_account);
        etPassword = (EditText) llPassword.findViewById(R.id.et_account);

        //显示密码的图片，默认不显示
        ivNoHide = (ImageView) findViewById(R.id.iv_no_hide);

        //“X”（清空输入）图片
        accountClear = (ImageView) llAccount.findViewById(R.id.iv_clear);
        passwordClear = (ImageView) llPassword.findViewById(R.id.iv_clear);

        //“记住密码”和“自动登入”
        llRemPassword = (LinearLayout) findViewById(R.id.ll_remember_password);
        llAutoLogin = (LinearLayout) findViewById(R.id.ll_auto_login);

        tvRemPassword = (TextView) llRemPassword.findViewById(R.id.tv_after_checkbox);
        tvAutoLogin = (TextView) llAutoLogin.findViewById(R.id.tv_after_checkbox);

        cbRemPassword = (CheckBox) llRemPassword.findViewById(R.id.check_box);
        cbAutoLogin = (CheckBox) llAutoLogin.findViewById(R.id.check_box);

        //“登录”和“注册”按钮
        btLogin = (Button) findViewById(R.id.bt_login);
        btRegister = (Button) findViewById(R.id.bt_register);
    }

    private void initViews() {
        tvPassword.setText(R.string.pass_word);
        etPassword.setHint(R.string.password_hint);
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        tvAutoLogin.setText(R.string.auto_login);

        accountClear.setTag(ACCOUNT_CLEAR);
        passwordClear.setTag(PASSWORD_CLEAR);

        //显示上一次登录成功的账号
        SharedPreferences sp=getSharedPreferences(ConstField.REMEMBER_USER_INFO,MODE_PRIVATE);
        etAccount.setText(sp.getString(ConstField.ACCOUNT, null));

        //根据SharePreference的值，设置“记住密码”和“自动登入”是否勾选
        if(sp.getBoolean(ConstField.REMEMBER_USER,false)){
            cbRemPassword.setChecked(true);
            etAccount.setText(sp.getString(ConstField.ACCOUNT, null));
            etPassword.setText(sp.getString(ConstField.PASSWORD,null));
        }else{
            cbRemPassword.setChecked(false);
        }

        if(sp.getBoolean(ConstField.AUTO_LOGIN,false)){
            cbAutoLogin.setChecked(true);
            Intent intent=new Intent(mContext, EnterActivity.class);
            Bundle bundle=new Bundle();
            bundle.putBoolean(ConstField.AUTO_LOGIN, true);
            bundle.putString(ConstField.ACCOUNT, etAccount.getText().toString());
            intent.putExtras(bundle);
            startActivityForResult(intent, ConstField.REQUEST_CODE);
        }
    }

    private void initListeners() {
        ivNoHide.setOnClickListener(this);
        accountClear.setOnClickListener(this);
        passwordClear.setOnClickListener(this);

        etAccount.addTextChangedListener(new MyTextWatcher(etAccount, mContext));
        etPassword.addTextChangedListener(new MyTextWatcher(etPassword, mContext));

        btLogin.setOnClickListener(this);
        btRegister.setOnClickListener(this);

        //“自动登录”勾选时，“记住密码”也勾选上
        cbAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(!cbRemPassword.isChecked()){
                        cbRemPassword.setChecked(true);
                    }
                }
            }
        });

        //“记住密码”一旦勾选上，“自动登录”始终是勾选的
        cbRemPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    if(cbAutoLogin.isChecked()){
                        cbRemPassword.setChecked(true);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        File file = new File(Environment.getExternalStorageDirectory(),fileName);

        switch (v.getId()){
            case R.id.iv_no_hide:
                if(showPassword){
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showPassword=!showPassword;
                }else {
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showPassword=!showPassword;
                }
                Editable editable=etPassword.getText();
                Selection.setSelection(editable,editable.length());
                break;
            case R.id.iv_clear:
                if(ACCOUNT_CLEAR.equalsIgnoreCase((String) v.getTag())){
                    etAccount.setText("");
                }else if(PASSWORD_CLEAR.equalsIgnoreCase((String) v.getTag())){
                    etPassword.setText("");
                }
                break;
            case R.id.bt_login:

                if(emptyCheck()){
                    break;
                }

                if(file.exists()){
                    judgeAndResult(file);
                }else{
                    ToastUtils.showMessage(mContext,"账号不存在！",ConstField.TOAST_DURATION);
                }
                break;
            case R.id.bt_register:
                FileOutputStream outputStream=null;

                if(emptyCheck()){
                    break;
                }

                //新建一个账号信息
                if(!file.exists()){
                    registerAccount(file, outputStream);
                }
                //检查账号是否已注册，已注册则不允许注册
                else {
                    if(accountExists(file,etAccount.getText().toString())){
                        ToastUtils.showMessage(mContext,"账号已存在，换个账号试试！",ConstField.TOAST_DURATION);
                    }else{
                        registerAccount(file, outputStream);
                    }
                }
                break;
        }
    }

    private void judgeAndResult(File file) {
        String accountTemp=etAccount.getText().toString();
        String passwordTemp=etPassword.getText().toString();
        //账号存在，则检验密码是否匹配
        if(accountExists(file, accountTemp)){
            if(strPassword!=null && strPassword.equals(passwordTemp)){
                ToastUtils.showMessage(mContext, "登录成功！", ConstField.TOAST_DURATION);

                //如果勾选了“记住密码”，则将该账号密码写入SharePreference中
                writeToRememberUserInfo(accountTemp, passwordTemp);

                //如果勾选了“自动登录”，则将该选项写入SharePreference中
                boolean autoLogin=writeToAutoLogin(accountTemp, passwordTemp);

                //跳转进入下一页面
                Intent intent=new Intent(mContext, EnterActivity.class);
                Bundle bundle=new Bundle();
                bundle.putBoolean(ConstField.AUTO_LOGIN, autoLogin);
                bundle.putString(ConstField.ACCOUNT, accountTemp);
                intent.putExtras(bundle);
//                startActivity(intent);
                startActivityForResult(intent,ConstField.REQUEST_CODE);

            }else {
                ToastUtils.showMessage(mContext,"密码错误！",ConstField.TOAST_DURATION);
            }
        }else{
            ToastUtils.showMessage(mContext, "账号不存在", ConstField.TOAST_DURATION);
        }
    }

    private boolean writeToAutoLogin(String accountTemp, String passwordTemp) {
        writeToRememberUserInfo(accountTemp,passwordTemp);
        boolean autoLogin=false;
        SharedPreferences sp=getSharedPreferences(ConstField.REMEMBER_USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        if(cbAutoLogin.isChecked()){
            editor.putBoolean(ConstField.AUTO_LOGIN, true);
            autoLogin=true;
        }else{
            editor.putBoolean(ConstField.AUTO_LOGIN, false);
            autoLogin=false;
        }
        editor.commit();
        return autoLogin;
    }

    private void writeToRememberUserInfo(String accountTemp, String passwordTemp) {
        SharedPreferences sp=getSharedPreferences(ConstField.REMEMBER_USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        if(cbRemPassword.isChecked()){
            editor.putBoolean(ConstField.REMEMBER_USER, true);
            editor.putString(ConstField.ACCOUNT, accountTemp);
            editor.putString(ConstField.PASSWORD, passwordTemp);
        }else {
            editor.putBoolean(ConstField.REMEMBER_USER, false);
//            editor.putString(ConstField.ACCOUNT, "");
            editor.putString(ConstField.PASSWORD, "");

            //即便不“记住密码”，也将其账号记录下来，当下次启动时，显示之前登陆的账号
            editor.putString(ConstField.ACCOUNT, accountTemp);
        }
        editor.commit();
    }

    //验证账号是否已经存在，顺便保存一下该账号对应的密码，以便登录时直接验证
    private boolean accountExists(File file, String originAccount) {
        if(file.exists()){
            try {
                FileInputStream inputStream=new FileInputStream(file);
                InputStreamReader streamReader=new InputStreamReader(inputStream,ConstField.DECODEING);
                BufferedReader reader=new BufferedReader(streamReader);
                String oneLine;
                while((oneLine=reader.readLine())!=null){
                    String[] userInfo=oneLine.split(ConstField.INFO_SEPARATOR);
                    if(originAccount!=null && originAccount.equalsIgnoreCase(userInfo[0])){
                        strPassword = userInfo[1];
                        return true;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            throw new RuntimeException("文件不存在异常！");
        }
        return false;
    }

    private boolean emptyCheck() {
        //检测账号是否为空
        if(isEmpty(etAccount)){
            ToastUtils.showMessage(mContext,"账号为空！",ConstField.TOAST_DURATION);
            return true;
        }
        //检测密码是否为空
        else if(isEmpty(etPassword)){
            ToastUtils.showMessage(mContext,"密码为空！",ConstField.TOAST_DURATION);
            return true;
        }else {
            return false;
        }
    }

    private void registerAccount(File file, FileOutputStream outputStream) {
        //判断外置SD卡是否可用
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

            //检测外置SD卡的可用容量
//            Log.d("XDJ","TotalMemory:"+GetMemoryUtil.getTotalMemory(mContext,file.getParent())
//            +",UsedMemory="+GetMemoryUtil.getUsedMemory(mContext,file.getParent())
//            +",AvaiableMemory="+GetMemoryUtil.getAvaiableMemory(mContext,file.getParent()));
            if(GetMemoryUtil.getAvaiableMemorySize(mContext,file.getParent())<ConstField.MIN_MEMORY_SIZE){
                ToastUtils.showMessage(mContext,"SD卡容量不足！注册失败！", ConstField.TOAST_DURATION);
                return;
            }
            String account=etAccount.getText().toString();
            String password=etPassword.getText().toString();
            if(account.length()<ConstField.MIN_LENGTH || account.length()>ConstField.MAX_LENGTH){
                ToastUtils.showMessage(mContext,"用户名的长度必须介于6-18个字符！",ConstField.TOAST_DURATION);
                return;
            }else if(password.length()<ConstField.MIN_LENGTH){
                ToastUtils.showMessage(mContext,"密码的长度必须大于6个字符！",ConstField.TOAST_DURATION);
            }else {
//                Log.d("XDJ","password.matches(0-9)="+(Pattern.compile("[0-9]+").matcher(password).find()));
//                Log.d("XDJ","password.matches([a-zA-Z])="+(Pattern.compile("[a-zA-Z]+").matcher(password).find()));
//                Log.d("XDJ","password.matches( )="+Pattern.compile("\\s+").matcher(password).find());
                if(password.length()>ConstField.MAX_LENGTH){
                    ToastUtils.showMessage(mContext,"密码的长度必须小于18个字符！",ConstField.TOAST_DURATION);
                }else{
                    if(Pattern.compile("\\s+").matcher(password).find()){
                        ToastUtils.showMessage(mContext,"密码含有非法字符！",ConstField.TOAST_DURATION);
                    }else {
                        if((Pattern.compile("[0-9]+").matcher(password).find()) &&
                                (Pattern.compile("[a-zA-Z]+").matcher(password).find())){
                            try {
                                outputStream=new FileOutputStream(file,true);
                                outputStream.write(account.getBytes());
                                outputStream.write(ConstField.INFO_SEPARATOR.getBytes());
                                outputStream.write(password.getBytes());
                                outputStream.write("\n".getBytes());
                                ToastUtils.showMessage(mContext, "注册成功！", ConstField.TOAST_DURATION);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }finally {
                                //释放文件资源
                                try {
                                    if(outputStream!=null){
                                        outputStream.close();
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }else {
                            ToastUtils.showMessage(mContext,"密码必须是字母和数字的组合！",ConstField.TOAST_DURATION);
                        }
                    }

                }
            }
        }
        else {
            ToastUtils.showMessage(mContext,"外置SD卡不可用！",ConstField.TOAST_DURATION);
        }
    }

    private boolean isEmpty(EditText et) {
        if(et.getText().length()==0){
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("XDJ", "onActivityResult()");
        String lastAccount=data.getStringExtra(ConstField.ACCOUNT);
        switch(resultCode){
            case RESULT_OK:
                etAccount.setText(lastAccount);
                etPassword.setText("");

                //退出登录时，应取消自动登录和记住密码
                cbAutoLogin.setChecked(false);
                cbRemPassword.setChecked(false);

                writeToAutoLogin(lastAccount, "");
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        writeToRememberUserInfo(etAccount.getText().toString(),etPassword.getText().toString());
        super.onBackPressed();
    }
}
