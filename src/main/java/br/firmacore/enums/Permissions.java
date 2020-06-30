package br.firmacore.enums;

public enum Permissions {
    ADMIN("firmacore.admin"), VIP("firmacore.vip");

    public final String permission;

    Permissions(String permission) {
        this.permission = permission;
    }

    public String getPermission(){
        return this.permission;
    }
}
