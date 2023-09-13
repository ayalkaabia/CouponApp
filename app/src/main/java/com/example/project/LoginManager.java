package com.example.project;
import android.content.Context;

import java.sql.SQLException;


public class LoginManager {


    private static Context context;

    private static LoginManager instance = null;
    private LoginManager(){}

    public static LoginManager getInstance(Context ctx)
    {
        context = ctx;
        if(instance == null) instance = new LoginManager();
        return instance;
    }

    public Object login(String email, String password, ClientType clientType) throws SQLException {

        switch (clientType) {
            case ADMINISTRATOR:
                AdminFacade adminFacade = AdminFacade.getInstance(context);
                if(adminFacade.login(email,password)==true)
                    return adminFacade;
                return null;
            case CUSTOMER:
                CustomersFacade customerFacade= new CustomersFacade(context);
                if (customerFacade.login(email,password)==true)
                    return  customerFacade;
                else
                    return null;

            case COMPANY:
                CompanyFacade companyFacade= new CompanyFacade(context);
                if (companyFacade.login(email,password)==true)
                    return  companyFacade;
                else
                    return null;

            default:
                return null;
        }
    }


}