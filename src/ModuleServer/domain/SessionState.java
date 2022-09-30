package ModuleServer.domain;

import ModuleCommon.valueobjects.LogInPhase;
import ModuleCommon.valueobjects.User;
import ModuleCommon.valueobjects.UserType;

import java.util.ArrayList;
import java.util.List;

/**
 * Class where the state of the current Session is saved inside of variables.
 *
 * @author Chachulski, Mathea
 */
public class SessionState {
    protected static LogInPhase logInPhase = null;
    protected static UserType userType = null;
    public static User currentUser = null;

    public static List<User> onlineUsers = new ArrayList<>();

    public SessionState(){

    }

}
