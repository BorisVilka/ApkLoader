package org.cryptonews.main;

import android.content.Context;
import android.util.Log;

import org.cryptonews.main.ui.activities.MyApp;
import org.cryptonews.main.ui.list_utils.ListItem;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils {

    private static Context context;

    public Utils(Context context1) {
        context = context1;
    }

    public static String getCirculating_supply_string(String s) {
        double circ = Double.parseDouble(s.replace(',','.'));
        if(context.getSharedPreferences(MyApp.prefs,Context.MODE_PRIVATE).getBoolean(MyApp.marketInfo,true)) {
            if(circ>=1_000_000_000) {
                return String.format("%.1f",((double)circ/1_000_000_000))+"B";
            } else if(circ>=1_000_000) {
                return String.format("%.1f",((double)circ/1_000_000))+"M";
            } else return String.valueOf(circ);
        } else return String.format("%.0f",circ);
    }

    public static String intSeparator(String s) {
        if(s==null) return "N/A";
        StringBuilder builder = new StringBuilder(String.format("%.0f",Double.parseDouble(s.replace(',','.'))));
        builder.reverse();
        for(int i = 3;i<builder.length();i+=4) builder.insert(i,' ');
        return builder.reverse().toString();
    }
    public static String intSeparatorDot(String s) {
        if(s==null) return "N/A";
        StringBuilder builder = new StringBuilder(String.format("%.2f",Double.parseDouble(s.replace(',','.'))));
        builder.reverse();
        for(int i = 6;i<builder.length();i+=4) builder.insert(i,'.');
        return builder.reverse().toString();
    }

    public static int convertDate(String date) {
        int ans = 0;
        String[] s = date.trim().split("\\s+");
        String[] dates = s[0].split("\\."), time = s[1].split("\\:");
        ans += (Integer.parseInt(dates[0])+Integer.parseInt(dates[1])*10+Integer.parseInt(dates[2])*100)*10000;
        ans += Integer.parseInt(time[1])+Integer.parseInt(time[0])*100;
        return ans;
    }

    public static String getSortType() {
        return context.getResources().getStringArray(R.array.sort_types)[context.getSharedPreferences(MyApp.prefs,Context.MODE_PRIVATE).getInt(MyApp.checked_index,1)];
    }

    public static String getSortOrder() {
        return context.getResources().getStringArray(R.array.sort_order)[context.getSharedPreferences(MyApp.prefs,Context.MODE_PRIVATE).getInt(MyApp.checked_index,1)%2];
    }
    public static String getSortTypeFav() {
        return context.getResources().getStringArray(R.array.sort_types)[context.getSharedPreferences(MyApp.prefs,Context.MODE_PRIVATE).getInt(MyApp.checked_index_fav,1)];
    }

    public static String getSortOrderFav() {
        return context.getResources().getStringArray(R.array.sort_order)[context.getSharedPreferences(MyApp.prefs,Context.MODE_PRIVATE).getInt(MyApp.checked_index_fav,1)%2];
    }


    public static Context getContext() {return context;}

    public static String getFavoritesQuery(List<String> list,int start, int end) {
        StringBuilder query = new StringBuilder();
        for(int i = start;i<=start+end && i<list.size();i++) query.append(list.get(i)+',');
        if(query.length()>0) query.deleteCharAt(query.length()-1);
        Log.d("TAG",query.toString());
        return query.toString();
    }

    public static void favoritesMove(ListItem item, boolean checked) {
        Set<String> set = getContext().getSharedPreferences(MyApp.prefs,Context.MODE_PRIVATE).getStringSet(MyApp.favorites,new HashSet<>());
        Set<String> newSet = new HashSet<>(set);
        if(checked) {
            newSet.add(String.valueOf(item.getCoin().getId()));
        } else newSet.remove(String.valueOf(item.getCoin().getId()));
        getContext().getSharedPreferences(MyApp.prefs,Context.MODE_PRIVATE).edit().putStringSet(MyApp.favorites,newSet).commit();
    }

    public static boolean containsFavorites(ListItem item) {
       return MyApp.getUtils().getContext()
                .getSharedPreferences(MyApp.prefs, Context.MODE_PRIVATE)
                .getStringSet(MyApp.favorites, new HashSet<>())
                .contains(String.valueOf(item.getCoin().getId()));
    }

    public static String getDate(float x, int id) {
        long d = (long)(x*1_000f);
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(d), ZoneId.systemDefault());
        String format = "";
        switch (id) {
            case 0 :
                format = "HH:mm";
                break;
            default:
                format = "dd.MM.yyyy";
                break;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(format));
    }


    public static LocalDateTime getTime(int ind) {
        switch (ind) {
           case 0 :
                return LocalDateTime.now().minusDays(1);
            case 1 :
                return LocalDateTime.now().minusWeeks(1);
            case 2 :
                return LocalDateTime.now().minusMonths(1);
            case 3 :
                return LocalDateTime.now().minusYears(1);
        }
        return null;
    }
}
