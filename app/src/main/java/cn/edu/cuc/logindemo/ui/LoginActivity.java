package cn.edu.cuc.logindemo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.edu.cuc.logindemo.R;
import cn.edu.cuc.logindemo.Utils.ToastHelper;
import cn.edu.cuc.logindemo.dao.UserDao;
import cn.edu.cuc.logindemo.domain.User;
import cn.edu.cuc.logindemo.userlayout.ValidationEditText;

public class LoginActivity extends Activity implements View.OnClickListener{

    private Button loginBtn;
    private ValidationEditText vetxtUser;
    private ValidationEditText vetxtPassword;

    private UserDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
    }

    /**
     * 初始化
     */
    private void initialize(){

        loginBtn = findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(this);

        vetxtUser = (ValidationEditText) findViewById(R.id.vetxt_username);
        vetxtPassword = (ValidationEditText)findViewById(R.id.vetxt_password);

        dao = new UserDao(LoginActivity.this);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.btn_login:

                //TODO 需要访问数据库，匹配用户名、密码，验证成功转入下一页TabsActivity
                User user = new User();
                user.setUsername(this.vetxtUser.getText().toString());
                user.setPassword(this.vetxtPassword.getText().toString());

                if(dao.checkUser(user)){
                    startActivity(new Intent(LoginActivity.this,TabsActivity.class));
                    finish();
                }else{
                    ToastHelper.showToast(getString(R.string.login_db_validate_error),Toast.LENGTH_LONG);
                }
        }



    }
}
