package com.sirajsaleem.mathmanica;

import java.util.ArrayList;

public class Choices {

    public ArrayList<Integer> createVeryEasyChoices(int correctAnswer) {
        ArrayList<Integer> choices = new ArrayList<>();
        while (choices.size() < 3) {
            int choiceNum = (int) (Math.random() * 10);
            if (correctAnswer > 10) {
                choiceNum = choiceNum + 9;
            }
            if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                choices.add(choiceNum);
            }
        }
        return choices;
    }

    public ArrayList<Integer> createEasyChoices(int correctAnswer, int userScore) {
        ArrayList<Integer> choices = new ArrayList<>();
        if (userScore < 150) {
            while (choices.size() < 3) {
                int choiceNum = (int) (Math.random() * 10);
                if (correctAnswer > 10) {
                    choiceNum = choiceNum + 9;
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (userScore < 375) {
            while (choices.size() < 3) {
                int choiceNum = (int) (Math.random() * 20);
                if (correctAnswer > 20) {
                    choiceNum = choiceNum + 19;
                } else if (correctAnswer < 10) {
                    choiceNum = choiceNum / 2;
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (userScore < 750) {
            while (choices.size() < 3) {
                int choiceNum = (int) (Math.random() * 50);
                if (correctAnswer > 50) {
                    choiceNum = choiceNum + 49;
                } else if (correctAnswer < 10) {
                    choiceNum = choiceNum / 5;
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (userScore < 1125) {
            while (choices.size() < 3) {
                int choiceNum = (int) (Math.random() * 100);
                if (correctAnswer > 100) {
                    choiceNum = choiceNum + 99;
                } else if (correctAnswer < 10) {
                    choiceNum = choiceNum / 10;
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else {
            while (choices.size() < 3) {
                int choiceNum = (int) (Math.random() * 1000);
                if (correctAnswer > 1000) {
                    choiceNum = choiceNum + 999;
                } else if (correctAnswer < 100 && correctAnswer >= 10) {
                    choiceNum = choiceNum / 10;
                } else if (correctAnswer < 10) {
                    choiceNum = choiceNum / 100;
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        }
        return choices;
    }

    public ArrayList<Integer> createMediumChoices(int correctAnswer, int userScore, String operation) {
        ArrayList<Integer> choices = new ArrayList<>();
        if (userScore < 100) {
            while (choices.size() < 3) {
                int choiceNum = (int) (Math.random() * 100);
                if (correctAnswer > 100) {
                    choiceNum = choiceNum + 99;
                } else if (correctAnswer < 10) {
                    choiceNum = choiceNum / 10;
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (userScore < 200) {
            while (choices.size() < 3) {
                int choiceNum = (int) (Math.random() * 1000);
                if (correctAnswer > 1000) {
                    choiceNum = choiceNum + 999;
                } else if (correctAnswer < 100 && correctAnswer >= 10) {
                    choiceNum = choiceNum / 10;
                } else if (correctAnswer < 10) {
                    choiceNum = choiceNum / 100;
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (userScore < 500) {
            while (choices.size() < 3) {
                int choiceNum;
                if (correctAnswer <= 10) {
                    choiceNum = (int) (Math.random() * 10);
                } else {
                    choiceNum = (int) (Math.random() * 100);
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (userScore < 1000) {
            while (choices.size() < 3) {
                int choiceNum;
                if (correctAnswer <= 100) {
                    choiceNum = (int) (Math.random() * 100);
                } else {
                    choiceNum = (int) (Math.random() * 1000); //can improve difficulty by adding *500 and *1000
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else {
            while (choices.size() < 3) {
                int choiceNum;
                if (operation.equals("+") || operation.equals("-")) {
                    choiceNum = (int) (Math.random() * 1000);
                    if (correctAnswer > 1000) {
                        choiceNum = choiceNum + 999;
                    } else if (correctAnswer < 100 && correctAnswer >= 10) {
                        choiceNum = choiceNum / 10;
                    } else if (correctAnswer < 10) {
                        choiceNum = choiceNum / 100;
                    }
                } else {
                    if (correctAnswer <= 1000) {
                        choiceNum = (int) (Math.random() * 1000);
                    } else {
                        choiceNum = (int) (Math.random() * 10000); //can improve difficulty by adding *500 and *1000
                    }
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        }
        return choices;
    }

    public ArrayList<Integer> createHardChoices(int correctAnswer, int userScore, String operation) {
        ArrayList<Integer> choices = new ArrayList<>();
        if (userScore < 125) {
            while (choices.size() < 3) {
                int choiceNum;
                if (correctAnswer <= 10) {
                    choiceNum = (int) (Math.random() * 10);
                } else if (correctAnswer <= 100) {
                    choiceNum = (int) (Math.random() * 100);
                } else if (correctAnswer < 400) {
                    choiceNum = (int) (Math.random() * 400);
                } else {
                    choiceNum = (int) (Math.random() * 900);
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (userScore < 500) {
            while (choices.size() < 3) {
                int choiceNum;
                if (correctAnswer <= 10) {
                    choiceNum = (int) (Math.random() * 10);
                } else if (correctAnswer <= 100) {
                    choiceNum = (int) (Math.random() * 100);
                } else if (correctAnswer <= 500) {
                    choiceNum = (int) (Math.random() * 500);
                } else if (correctAnswer <= 1000) {
                    choiceNum = (int) (Math.random() * 1000);
                } else if (correctAnswer <= 2500) {
                    choiceNum = (int) (Math.random() * 2500);
                } else if (correctAnswer <= 5000) {
                    choiceNum = (int) (Math.random() * 5000);
                } else {
                    choiceNum = (int) (Math.random() * 10000);
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (userScore < 875) {
            while (choices.size() < 3) {
                int choiceNum;
                if (correctAnswer <= 10) {
                    choiceNum = (int) (Math.random() * 10);
                } else if (correctAnswer <= 100) {
                    choiceNum = (int) (Math.random() * 100);
                } else if (correctAnswer <= 1000) {
                    choiceNum = (int) (Math.random() * 1000);
                } else if (correctAnswer <= 2500) {
                    choiceNum = (int) (Math.random() * 2500);
                } else if (correctAnswer <= 5000) {
                    choiceNum = (int) (Math.random() * 5000);
                } else if (correctAnswer <= 7500) {
                    choiceNum = (int) (Math.random() * 7500);
                } else if (correctAnswer <= 10000) {
                    choiceNum = (int) (Math.random() * 10000);
                } else if (correctAnswer <= 20000) {
                    choiceNum = (int) (Math.random() * 20000);
                } else if (correctAnswer <= 40000) {
                    choiceNum = (int) (Math.random() * 40000);
                } else if (correctAnswer <= 60000) {
                    choiceNum = (int) (Math.random() * 60000);
                } else if (correctAnswer <= 80000) {
                    choiceNum = (int) (Math.random() * 80000);
                } else {
                    choiceNum = (int) (Math.random() * 100000);
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else {
            while (choices.size() < 3) {
                int choiceNum;
                if (operation.equals("+") || operation.equals("-")) {
                    choiceNum = (int) (Math.random() * 10000);
                    if (correctAnswer > 10000) {
                        choiceNum = choiceNum + 9999;
                    } else if (correctAnswer <= 1000) {
                        choiceNum = choiceNum / 10;
                    }
                } else {
                    if (correctAnswer <= 10) {
                        choiceNum = (int) (Math.random() * 10);
                    } else if (correctAnswer <= 100) {
                        choiceNum = (int) (Math.random() * 100);
                    } else if (correctAnswer <= 1000) {
                        choiceNum = (int) (Math.random() * 1000);
                    } else if (correctAnswer <= 2500) {
                        choiceNum = (int) (Math.random() * 2500);
                    } else if (correctAnswer <= 5000) {
                        choiceNum = (int) (Math.random() * 5000);
                    } else if (correctAnswer <= 7500) {
                        choiceNum = (int) (Math.random() * 7500);
                    } else if (correctAnswer <= 10000) {
                        choiceNum = (int) (Math.random() * 10000);
                    } else if (correctAnswer <= 20000) {
                        choiceNum = (int) (Math.random() * 20000);
                    } else if (correctAnswer <= 40000) {
                        choiceNum = (int) (Math.random() * 40000);
                    } else if (correctAnswer <= 60000) {
                        choiceNum = (int) (Math.random() * 60000);
                    } else if (correctAnswer <= 80000) {
                        choiceNum = (int) (Math.random() * 80000);
                    } else if (correctAnswer <= 100000) {
                        choiceNum = (int) (Math.random() * 100000);
                    } else if (correctAnswer <= 250000) {
                        choiceNum = (int) (Math.random() * 250000);
                    } else if (correctAnswer <= 500000) {
                        choiceNum = (int) (Math.random() * 500000);
                    } else {
                        choiceNum = (int) (Math.random() * 1000000);
                    }
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        }
        return choices;
    }

    public ArrayList<Integer> createStreakChoices(int correctAnswer, int streakNum, String operation) {
        ArrayList<Integer> choices = new ArrayList<>();
        if (streakNum <= 2) {
            while (choices.size() < 3) {
                int choiceNum = (int) (Math.random() * 10);
                if (correctAnswer > 10) {
                    choiceNum = choiceNum + 9;
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (streakNum <= 4) {
            while (choices.size() < 3) {
                int choiceNum = (int) (Math.random() * 100);
                if (correctAnswer > 100) {
                    choiceNum = choiceNum + 99;
                } else if (correctAnswer < 10) {
                    choiceNum = choiceNum / 10;
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (streakNum <= 6) {
            while (choices.size() < 3) {
                int choiceNum;
                if (correctAnswer <= 10) {
                    choiceNum = (int) (Math.random() * 10);
                } else {
                    choiceNum = (int) (Math.random() * 100);
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (streakNum <= 8) {
            while (choices.size() < 3) {
                int choiceNum = (int) (Math.random() * 1000);
                if (correctAnswer > 1000) {
                    choiceNum = choiceNum + 999;
                } else if (correctAnswer < 100 && correctAnswer >= 10) {
                    choiceNum = choiceNum / 10;
                } else if (correctAnswer < 10) {
                    choiceNum = choiceNum / 100;
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (streakNum <= 10) {
            while (choices.size() < 3) {
                int choiceNum;
                if (correctAnswer <= 100) {
                    choiceNum = (int) (Math.random() * 100);
                } else {
                    choiceNum = (int) (Math.random() * 1000);
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (streakNum <= 12) {
            while (choices.size() < 3) {
                int choiceNum;
                choiceNum = (int) (Math.random() * 10000);
                if (correctAnswer > 10000) {
                    choiceNum = choiceNum + 9999;
                } else if (correctAnswer <= 1000 && correctAnswer > 100) {
                    choiceNum = choiceNum / 10;
                } else if (correctAnswer <= 100 && correctAnswer >= 10) {
                    choiceNum = choiceNum / 100;
                } else if (correctAnswer <= 10) {
                    choiceNum = choiceNum / 1000;
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (streakNum <= 14) {
            while (choices.size() < 3) {
                int choiceNum;
                if (correctAnswer <= 10) {
                    choiceNum = (int) (Math.random() * 10);
                } else if (correctAnswer <= 100) {
                    choiceNum = (int) (Math.random() * 100);
                } else if (correctAnswer <= 500) {
                    choiceNum = (int) (Math.random() * 500);
                } else if (correctAnswer <= 1000) {
                    choiceNum = (int) (Math.random() * 1000);
                } else if (correctAnswer <= 2500) {
                    choiceNum = (int) (Math.random() * 2500);
                } else if (correctAnswer <= 5000) {
                    choiceNum = (int) (Math.random() * 5000);
                } else {
                    choiceNum = (int) (Math.random() * 10000);
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (streakNum <= 16) {
            while (choices.size() < 3) {
                int choiceNum;
                if (correctAnswer <= 10) {
                    choiceNum = (int) (Math.random() * 10);
                } else if (correctAnswer <= 100) {
                    choiceNum = (int) (Math.random() * 100);
                } else if (correctAnswer <= 1000) {
                    choiceNum = (int) (Math.random() * 1000);
                } else if (correctAnswer <= 2500) {
                    choiceNum = (int) (Math.random() * 2500);
                } else if (correctAnswer <= 5000) {
                    choiceNum = (int) (Math.random() * 5000);
                } else if (correctAnswer <= 7500) {
                    choiceNum = (int) (Math.random() * 7500);
                } else if (correctAnswer <= 10000) {
                    choiceNum = (int) (Math.random() * 10000);
                } else if (correctAnswer <= 20000) {
                    choiceNum = (int) (Math.random() * 20000);
                } else if (correctAnswer <= 40000) {
                    choiceNum = (int) (Math.random() * 40000);
                } else if (correctAnswer <= 60000) {
                    choiceNum = (int) (Math.random() * 60000);
                } else if (correctAnswer <= 80000) {
                    choiceNum = (int) (Math.random() * 80000);
                } else {
                    choiceNum = (int) (Math.random() * 100000);
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (streakNum <= 20) {
            while (choices.size() < 3) {
                int choiceNum;
                if (correctAnswer <= 10) {
                    choiceNum = (int) (Math.random() * 10);
                } else if (correctAnswer <= 100) {
                    choiceNum = (int) (Math.random() * 100);
                } else if (correctAnswer <= 1000) {
                    choiceNum = (int) (Math.random() * 1000);
                } else if (correctAnswer <= 2500) {
                    choiceNum = (int) (Math.random() * 2500);
                } else if (correctAnswer <= 5000) {
                    choiceNum = (int) (Math.random() * 5000);
                } else if (correctAnswer <= 7500) {
                    choiceNum = (int) (Math.random() * 7500);
                } else if (correctAnswer <= 10000) {
                    choiceNum = (int) (Math.random() * 10000);
                } else if (correctAnswer <= 20000) {
                    choiceNum = (int) (Math.random() * 20000);
                } else if (correctAnswer <= 40000) {
                    choiceNum = (int) (Math.random() * 40000);
                } else if (correctAnswer <= 60000) {
                    choiceNum = (int) (Math.random() * 60000);
                } else if (correctAnswer <= 80000) {
                    choiceNum = (int) (Math.random() * 80000);
                } else if (correctAnswer <= 100000) {
                    choiceNum = (int) (Math.random() * 100000);
                } else if (correctAnswer <= 250000) {
                    choiceNum = (int) (Math.random() * 250000);
                } else if (correctAnswer <= 500000) {
                    choiceNum = (int) (Math.random() * 500000);
                } else {
                    choiceNum = (int) (Math.random() * 1000000);
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else {
            while (choices.size() < 3) {
                int choiceNum;
                if (operation.equals("+") || operation.equals("-")) {
                    choiceNum = (int) (Math.random() * 10000);
                    if (correctAnswer > 10000) {
                        choiceNum = choiceNum + 9999;
                    } else if (correctAnswer <= 1000 && correctAnswer > 100) {
                        choiceNum = choiceNum / 10;
                    } else if (correctAnswer <= 100 && correctAnswer >= 10) {
                        choiceNum = choiceNum / 100;
                    } else if (correctAnswer <= 10) {
                        choiceNum = choiceNum / 1000;
                    }
                } else {
                    if (correctAnswer <= 10) {
                        choiceNum = (int) (Math.random() * 10);
                    } else if (correctAnswer <= 100) {
                        choiceNum = (int) (Math.random() * 100);
                    } else if (correctAnswer <= 1000) {
                        choiceNum = (int) (Math.random() * 1000);
                    } else if (correctAnswer <= 2500) {
                        choiceNum = (int) (Math.random() * 2500);
                    } else if (correctAnswer <= 5000) {
                        choiceNum = (int) (Math.random() * 5000);
                    } else if (correctAnswer <= 7500) {
                        choiceNum = (int) (Math.random() * 7500);
                    } else if (correctAnswer <= 10000) {
                        choiceNum = (int) (Math.random() * 10000);
                    } else if (correctAnswer <= 20000) {
                        choiceNum = (int) (Math.random() * 20000);
                    } else if (correctAnswer <= 40000) {
                        choiceNum = (int) (Math.random() * 40000);
                    } else if (correctAnswer <= 60000) {
                        choiceNum = (int) (Math.random() * 60000);
                    } else if (correctAnswer <= 80000) {
                        choiceNum = (int) (Math.random() * 80000);
                    } else if (correctAnswer <= 100000) {
                        choiceNum = (int) (Math.random() * 100000);
                    } else if (correctAnswer <= 250000) {
                        choiceNum = (int) (Math.random() * 250000);
                    } else if (correctAnswer <= 500000) {
                        choiceNum = (int) (Math.random() * 500000);
                    } else {
                        choiceNum = (int) (Math.random() * 1000000);
                    }
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        }
        return choices;
    }
}






