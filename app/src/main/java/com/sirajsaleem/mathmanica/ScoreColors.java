package com.sirajsaleem.mathmanica;

import java.util.ArrayList;

public class ScoreColors {
    private final ArrayList<Integer> veryEasyColors = new ArrayList<>();
    private final ArrayList<Integer> easyColors = new ArrayList<>();
    private final ArrayList<Integer> mediumColors = new ArrayList<>();
    private final ArrayList<Integer> hardColors = new ArrayList<>();
    private final ArrayList<Integer> streakColors = new ArrayList<>();
    private static ScoreColors instance; //todo: check it out

    private ScoreColors() {
        veryEasyColors.add(R.color.very_easy_score_1);  //0
        veryEasyColors.add(R.color.very_easy_score_2);  //1
        veryEasyColors.add(R.color.very_easy_score_3);  //2
        veryEasyColors.add(R.color.very_easy_score_4);  //3
        veryEasyColors.add(R.color.very_easy_score_5);  //4
        veryEasyColors.add(R.color.very_easy_score_6);  //5
        veryEasyColors.add(R.color.very_easy_score_7);  //6
        veryEasyColors.add(R.color.very_easy_score_8);  //7
        veryEasyColors.add(R.color.very_easy_score_9);  //8
        veryEasyColors.add(R.color.very_easy_score_10);  //9
        veryEasyColors.add(R.color.very_easy_score_11);  //10
        veryEasyColors.add(R.color.very_easy_score_12);  //11
        veryEasyColors.add(R.color.very_easy_score_13);  //12
        veryEasyColors.add(R.color.very_easy_score_14);  //13

        easyColors.add(R.color.easy_score_1);  //0
        easyColors.add(R.color.easy_score_2);  //1
        easyColors.add(R.color.easy_score_3);  //2
        easyColors.add(R.color.easy_score_4);  //3
        easyColors.add(R.color.easy_score_5);  //4
        easyColors.add(R.color.easy_score_6);  //5
        easyColors.add(R.color.easy_score_7);  //0
        easyColors.add(R.color.easy_score_8);  //7
        easyColors.add(R.color.easy_score_9);  //8
        easyColors.add(R.color.easy_score_10);  //9
        easyColors.add(R.color.easy_score_11);  //10
        easyColors.add(R.color.easy_score_12);  //11
        easyColors.add(R.color.easy_score_13);  //12
        easyColors.add(R.color.easy_score_14);  //13

        mediumColors.add(R.color.medium_score_1);  //0
        mediumColors.add(R.color.medium_score_2);  //1
        mediumColors.add(R.color.medium_score_3);  //2
        mediumColors.add(R.color.medium_score_4);  //3
        mediumColors.add(R.color.medium_score_5);  //4
        mediumColors.add(R.color.medium_score_6);  //5
        mediumColors.add(R.color.medium_score_7);  //0
        mediumColors.add(R.color.medium_score_8);  //7
        mediumColors.add(R.color.medium_score_9);  //8
        mediumColors.add(R.color.medium_score_10);  //9
        mediumColors.add(R.color.medium_score_11);  //10
        mediumColors.add(R.color.medium_score_12);  //11
        mediumColors.add(R.color.medium_score_13);  //12
        mediumColors.add(R.color.medium_score_14);  //13

        hardColors.add(R.color.hard_score_1);  //0
        hardColors.add(R.color.hard_score_2);  //1
        hardColors.add(R.color.hard_score_3);  //2
        hardColors.add(R.color.hard_score_4);  //3
        hardColors.add(R.color.hard_score_5);  //4
        hardColors.add(R.color.hard_score_6);  //5
        hardColors.add(R.color.hard_score_7);  //0
        hardColors.add(R.color.hard_score_8);  //7
        hardColors.add(R.color.hard_score_9);  //8
        hardColors.add(R.color.hard_score_10);  //9
        hardColors.add(R.color.hard_score_11);  //10
        hardColors.add(R.color.hard_score_12);  //11
        hardColors.add(R.color.hard_score_13);  //12
        hardColors.add(R.color.hard_score_14);  //13

        streakColors.add(R.color.very_easy_score_1);  //0
        streakColors.add(R.color.very_easy_score_3);  //1
        streakColors.add(R.color.very_easy_score_6);  //2
        streakColors.add(R.color.very_easy_score_9);  //3
        streakColors.add(R.color.very_easy_score_12);  //4
        streakColors.add(R.color.easy_score_1);  //5
        streakColors.add(R.color.easy_score_4);  //0
        streakColors.add(R.color.easy_score_8);  //7
        streakColors.add(R.color.easy_score_12);  //8
        streakColors.add(R.color.medium_score_1);  //9
        streakColors.add(R.color.medium_score_4);  //10
        streakColors.add(R.color.medium_score_8);  //11
        streakColors.add(R.color.medium_score_11);  //12
        streakColors.add(R.color.hard_score_1);  //13
        streakColors.add(R.color.hard_score_3);  //14
        streakColors.add(R.color.hard_score_5);  //15
        streakColors.add(R.color.hard_score_7);  //16
        streakColors.add(R.color.hard_score_9);  //17
        streakColors.add(R.color.hard_score_10);  //18
        streakColors.add(R.color.hard_score_11);  //19
        streakColors.add(R.color.hard_score_13);  //20
        streakColors.add(R.color.hard_score_14);  //21
    }

    public int getColor(String gameDifficulty, int userScore) {
        switch (gameDifficulty) {
            case "Very_Easy":
                if (userScore <= 20) {
                    return veryEasyColors.get(0);
                } else if (userScore < 40) {
                    return veryEasyColors.get(1);
                } else if (userScore < 70) {
                    return veryEasyColors.get(2);
                } else if (userScore < 110) {
                    return veryEasyColors.get(3);
                } else if (userScore < 160) {
                    return veryEasyColors.get(4);
                } else if (userScore < 220) {
                    return veryEasyColors.get(5);
                } else if (userScore < 290) {
                    return veryEasyColors.get(6);
                } else if (userScore < 370) {
                    return veryEasyColors.get(7);
                } else if (userScore < 460) {
                    return veryEasyColors.get(8);
                } else if (userScore < 560) {
                    return veryEasyColors.get(9);
                } else if (userScore < 670) {
                    return veryEasyColors.get(10);
                } else if (userScore < 790) {
                    return veryEasyColors.get(11);
                } else if (userScore < 920) {
                    return veryEasyColors.get(12);
                } else {
                    return veryEasyColors.get(13);
                }
            case "Easy":
                if (userScore <= 30) {
                    return easyColors.get(0);
                } else if (userScore < 75) {
                    return easyColors.get(1);
                } else if (userScore < 120) {
                    return easyColors.get(2);
                } else if (userScore < 195) {
                    return easyColors.get(3);
                } else if (userScore < 285) {
                    return easyColors.get(4);
                } else if (userScore < 390) {
                    return easyColors.get(5);
                } else if (userScore < 510) {
                    return easyColors.get(6);
                } else if (userScore < 645) {
                    return easyColors.get(7);
                } else if (userScore < 795) {
                    return easyColors.get(8);
                } else if (userScore < 960) {
                    return easyColors.get(9);
                } else if (userScore < 1140) {
                    return easyColors.get(10);
                } else if (userScore < 1335) {
                    return easyColors.get(11);
                } else if (userScore < 1545) {
                    return easyColors.get(12);
                } else {
                    return easyColors.get(13);
                }
            case "Medium":
                if (userScore <= 40) {
                    return mediumColors.get(0);
                } else if (userScore < 100) {
                    return mediumColors.get(1);
                } else if (userScore < 140) {
                    return mediumColors.get(2);
                } else if (userScore < 200) {
                    return mediumColors.get(3);
                } else if (userScore < 350) {
                    return mediumColors.get(4);
                } else if (userScore < 500) {
                    return mediumColors.get(5);
                } else if (userScore < 750) {
                    return mediumColors.get(6);
                } else if (userScore < 1000) {
                    return mediumColors.get(7);
                } else if (userScore < 1250) {
                    return mediumColors.get(8);
                } else if (userScore < 1500) {
                    return mediumColors.get(9);
                } else if (userScore < 1600) {
                    return mediumColors.get(10);
                } else if (userScore < 1700) {
                    return mediumColors.get(11);
                } else if (userScore < 1800) {
                    return mediumColors.get(12);
                } else {
                    return mediumColors.get(13);
                }
            case "Streak":
                if (userScore <= 1) {
                    return streakColors.get(0);
                } else if (userScore == 2) {
                    return streakColors.get(1);
                } else if (userScore == 3) {
                    return streakColors.get(2);
                } else if (userScore == 4) {
                    return streakColors.get(3);
                } else if (userScore == 5) {
                    return streakColors.get(4);
                } else if (userScore == 6) {
                    return streakColors.get(5);
                } else if (userScore == 7) {
                    return streakColors.get(6);
                } else if (userScore == 8) {
                    return streakColors.get(7);
                } else if (userScore == 9) {
                    return streakColors.get(8);
                } else if (userScore == 10) {
                    return streakColors.get(9);
                } else if (userScore == 11) {
                    return streakColors.get(10);
                } else if (userScore == 12) {
                    return streakColors.get(11);
                } else if (userScore == 13) {
                    return streakColors.get(12);
                } else if (userScore == 14) {
                    return streakColors.get(13);
                } else if (userScore == 15) {
                    return streakColors.get(14);
                } else if (userScore == 16) {
                    return streakColors.get(15);
                } else if (userScore == 17) {
                    return streakColors.get(16);
                } else if (userScore == 18) {
                    return streakColors.get(17);
                } else if (userScore == 19) {
                    return streakColors.get(18);
                } else if (userScore == 20) {
                    return streakColors.get(19);
                } else if (userScore == 21) {
                    return streakColors.get(20);
                } else {
                    return streakColors.get(21);
                }
            case "Time_Trial": //same colors as streakColors but different levels
                if (userScore <= 20) {
                    return streakColors.get(0);
                } else if (userScore < 40) {
                    return streakColors.get(1);
                } else if (userScore < 60) {
                    return streakColors.get(2);
                } else if (userScore < 80) {
                    return streakColors.get(3);
                } else if (userScore < 100) {
                    return streakColors.get(4);
                } else if (userScore < 120) {
                    return streakColors.get(5);
                } else if (userScore < 140) {
                    return streakColors.get(6);
                } else if (userScore < 160) {
                    return streakColors.get(7);
                } else if (userScore < 180) {
                    return streakColors.get(8);
                } else if (userScore < 200) {
                    return streakColors.get(9);
                } else if (userScore < 220) {
                    return streakColors.get(10);
                } else if (userScore < 240) {
                    return streakColors.get(11);
                } else if (userScore < 260) {
                    return streakColors.get(12);
                } else if (userScore < 280) {
                    return streakColors.get(13);
                } else if (userScore < 300) {
                    return streakColors.get(14);
                } else if (userScore < 320) {
                    return streakColors.get(15);
                } else if (userScore < 340) {
                    return streakColors.get(16);
                } else if (userScore < 360) {
                    return streakColors.get(17);
                } else if (userScore < 380) {
                    return streakColors.get(18);
                } else if (userScore < 400) {
                    return streakColors.get(19);
                } else if (userScore < 420) {
                    return streakColors.get(20);
                } else {
                    return streakColors.get(21);
                }
            default: // "Hard"
                if (userScore < 50) {
                    return hardColors.get(0);
                } else if (userScore < 125) {
                    return hardColors.get(1);
                } else if (userScore < 200) {
                    return hardColors.get(2);
                } else if (userScore < 250) {
                    return hardColors.get(3);
                } else if (userScore < 375) {
                    return hardColors.get(4);
                } else if (userScore < 500) {
                    return hardColors.get(5);
                } else if (userScore < 625) {
                    return hardColors.get(6);
                } else if (userScore < 875) {
                    return hardColors.get(7);
                } else if (userScore < 1000) {
                    return hardColors.get(8);
                } else if (userScore < 1250) {
                    return hardColors.get(9);
                } else if (userScore < 1500) {
                    return hardColors.get(10);
                } else if (userScore < 1750) {
                    return hardColors.get(11);
                } else if (userScore < 2000) {
                    return hardColors.get(12);
                } else {
                    return hardColors.get(13);
                }
        }
    }

    public static ScoreColors getInstance() {
        return instance = new ScoreColors();
    }
}
