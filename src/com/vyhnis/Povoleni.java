package com.vyhnis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Povoleni {

    public static void main(String[] args) {
        File file = new File("D:\\Projects\\input.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int numberOfTasks = scanner.nextInt();
        for(int i = 0; i < numberOfTasks; i++){
            int numberOfPermissions = scanner.nextInt();
            int numberOfReadPermissions = 1;
            int numberOfRelations = scanner.nextInt();
            int mainPermission = scanner.nextInt();

            Permission[] permissions = new Permission[numberOfPermissions];
            permissions[0] = new Permission(mainPermission);
            for(int j = 0; j < numberOfRelations; j++){
                int permissionFor = scanner.nextInt();
                int permissionNeeded = scanner.nextInt();

                boolean permissionNeedsSave = true;
                for (Permission p : permissions){
                    if(p != null) {
                        if( p.id == permissionFor){
                            p.neededPermissions.add(permissionNeeded);
                            permissionNeedsSave = false;
                        }
                    }
                }
                if(permissionNeedsSave) {
                    permissions[numberOfReadPermissions] = new Permission(permissionFor);
                    permissions[numberOfReadPermissions].neededPermissions.add(permissionNeeded);
                    numberOfReadPermissions++;
                }
            }


            List<Integer> neededPermissions = new ArrayList<>();
            neededPermissions.add(mainPermission);
            List<Integer> possibilityLog = new ArrayList<>();
            try {
                addNeededPermissionsForPermission(mainPermission, neededPermissions, permissions, possibilityLog);
                System.out.print("\npujde to");

                for(int k = 0; k < neededPermissions.size()/2; k++){
                    int temp = neededPermissions.get(k);
                    neededPermissions.set(k, neededPermissions.get(neededPermissions.size()-1-k));
                    neededPermissions.set(neededPermissions.size()-1-k, temp);
                }

                neededPermissions = new ArrayList<>(new LinkedHashSet<>(neededPermissions));

                for(int permission : neededPermissions) {
                    System.out.print(" "+permission);
                }
            } catch (ArithmeticException e) {
                System.out.print("\najajaj");
            }
        }

    }

    public static void addNeededPermissionsForPermission(int permission, List<Integer> neededPermissions, Permission[] permissions, List<Integer> possibilityLog) throws ArithmeticException {
        if(possibilityLog.contains(permission)){
            throw new ArithmeticException("Ajajaj");
        }
        possibilityLog.add(permission);


        for (Permission p : permissions) {
            if (p != null && p.id == permission) {
               for (int i = 0; i < p.neededPermissions.size(); i++) {
                   if(possibilityLog.contains(p.neededPermissions.get(i))){
                       throw new ArithmeticException("Ajajaj");
                   }
                   neededPermissions.add(p.neededPermissions.get(i));
                   addNeededPermissionsForPermission(p.neededPermissions.get(i), neededPermissions, permissions, possibilityLog);

               }
            }
        }
        possibilityLog.remove(possibilityLog.size()-1);
        //System.out.println("Processing..");
    }

    public static class Permission {
        int id;
        List<Integer> neededPermissions;

        public Permission(int id) {
            this.id = id;
            neededPermissions = new ArrayList<>();
        }
    }
}




