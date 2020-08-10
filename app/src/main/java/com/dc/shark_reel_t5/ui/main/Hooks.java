package com.dc.shark_reel_t5.ui.main;

import android.util.Log;

import java.util.ArrayList;

public class Hooks {

    private static ArrayList<String[]> collection = new ArrayList<String[]>(0);

    private static Hooks instance;

    private static String[] dataTypes = {"Hook Number", "Time In", "Time Out", "Hook Size", "Bait", "Species", "Maturity", "Tag Number", "Hook Placement", "Sex", "Catch", "New/Recapture", "Site Name", "GPS Start", "Start Coordinate Type", "GPS End", "End Coordinate Type", "PCL", "FL", "TL", "Girth", "Release Condition", "NOAA Card", "Notes", "Time Out", "Landed", "Blood 1", "Blood 2", "Release", "Deck/Platform", "Depth Range", "Fishing Method, Break Off"};

    /**
     * String indices:
     * 00: Hook Number
     * 01: Time In
     * 02: Time Out
     * 03: Hook Size
     * 04: Bait
     * 05: Species
     * 06: Maturity
     * 07: Tag Number
     * 08: Hook Placement
     * 09: Sex
     * 10: Catch
     * 11: New/Recapture
     * 12: Site Name
     * 13: GPS Start
     * 14: Start Coordinate Type
     * 15: GPS End
     * 16: End Coordinate Type
     * 17: PCL
     * 18: FL
     * 19: TL
     * 20: Girth
     * 21: Release Condition
     * 22: NOAA Card
     * 23: Notes
     * 24: Shark On
     * 25: Landed
     * 26: Blood 1
     * 27: Blood 2
     * 28: Release
     * 29: Deck/Platform
     * 30: Depth Range
     * 31: Fishing Method
     * 32: Break
     */

    public static Hooks getInstance() {
        if (instance == null)
            instance = new Hooks();
        return instance;
    }

    private Hooks() {
        collection.add(new String[32]);
    }

    public static void changeData(int hook, int index, String data) {
        hook--;
        while (hook >= collection.size()) {
            collection.add(new String[32]);
            collection.get(collection.size() - 1)[13] = "Decimal Degrees";
            collection.get(collection.size() - 1)[15] = "Decimal Degrees";
        }
        collection.get(hook)[index] = data;
        return;
    }

    public static void addData() {
        collection.add(new String[32]);
        collection.get(collection.size() - 1)[13] = "Decimal Degrees";
        collection.get(collection.size() - 1)[15] = "Decimal Degrees";
        return;
    }
    public static void deleteData(int position) {
        collection.remove(position);
    }

    public static String getData(int hook, int index) {

        if (collection.get(hook)[index] == null) {

            return "";

        } else {

            return collection.get(hook)[index];

        }
    }

    public static int getHookAmount() {

        return collection.size();
    }

    public static boolean hookExists(int hook) {

        return hook >= collection.size();
    }

    public static int getCategoriesLength(int hook) {

        return collection.get(hook).length;
    }

    public static String getDataType(int index) {

        return dataTypes[index];

    }

    public static int getDataTypeLength() {

        return dataTypes.length;

    }

}
