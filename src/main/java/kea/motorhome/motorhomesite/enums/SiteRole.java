package kea.motorhome.motorhomesite.enums;

public enum SiteRole
{
    SALES,ADMIN,CUSTOMER,OTHER,ERROR;
    /** Retrieves a SiteRole based on content of supplied String */
    public static SiteRole from(String role){
        switch(role.toLowerCase()){
            case "sales":return SALES;
            case "admin":return ADMIN;
            case "customer":return CUSTOMER;
            case "other":return OTHER;
            default:return ERROR;
        }
    }
}
