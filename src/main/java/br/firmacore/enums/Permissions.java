package br.firmacore.enums;

public enum Permissions {
    ADMIN("firmacore.admin"), VIP("firmacore.vip"), MEMBER("firmacore.member");

    public final String permission;

    Permissions(String permission) {
        this.permission = permission;
    }

    public String getPermission(){
        return this.permission;
    }
}
