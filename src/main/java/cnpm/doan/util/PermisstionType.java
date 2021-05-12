package cnpm.doan.util;

public enum PermisstionType {
    DEFAULT(""),
    GET_ALL_USERS("Leader.GetAllUsers"),
    EDIT_USER("Leader.EditUser"),
    CREATE_USER("Leader.CreateUser"),
    DELETE_USER("Leader.DeleteUser"),
    GET_ALL_PROJECT("Leader.GetAllProjectsByLeaderId"),
    EDIT_PROJECT("Leader.EditProject"),
    CREATE_PROJECT("Leader.CreateProject"),
    DELETE_PROJECT("Leader.DeleteProject"),
    GET_ALL_TASK("Leader.GetAllTasksByProjectId"),
    EDIT_TASK("Leader.EditTask"),
    CREATE_TASK("Leader.CreateTask"),
    DELETE_TASK("Leader.DeleteTask"),
    GET_SCHEDULE("Leader.GetScheduleOfUser"),
    EDIT_SCHEDULE("Leader.EditSchedule");

    private String permissionName;

    PermisstionType(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionName() {
        return permissionName;
    }
}
