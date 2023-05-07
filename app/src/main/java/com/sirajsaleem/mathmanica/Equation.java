package com.sirajsaleem.mathmanica;

public class Equation {

    private int firstNum;
    private int secondNum;

    public int getFirstNum() {
        return firstNum;
    }

    public void setFirstNum(int firstNum) {
        this.firstNum = firstNum;
    }

    public int getSecondNum() {
        return secondNum;
    }

    public void setSecondNum(int secondNum) {
        this.secondNum = secondNum;
    }

    public int getAnswer(int firstNum, String operation, int secondNum, String gameDifficulty, int userScore) {
        int correctAnswer;
        switch (operation) {
            case "+":
                correctAnswer = firstNum + secondNum;
                break;
            case "-":
                if (firstNum >= secondNum) {
                    correctAnswer = firstNum - secondNum;
                } else {
                    correctAnswer = secondNum - firstNum;
                }
                break;
            case "×":
                correctAnswer = firstNum * secondNum;
                break;
            default: // case divide ÷
                if (gameDifficulty.equals("Medium")) {
                    correctAnswer = mediumDivisionFixer(firstNum, operation, secondNum, userScore);
                } else if(gameDifficulty.equals("Hard")) {
                    correctAnswer = hardDivisionFixer(firstNum, operation, secondNum, userScore);
                } else { // streak
                    correctAnswer = streakDivisionFixer(firstNum, operation, secondNum, userScore);
                }
                break;
        }
        return correctAnswer;
    }

    public int mediumDivisionFixer(int firstNum, String operation, int secondNum, int userScore) {
        int correctAnswer = -1;
        double multiOrDivide = Math.random() * 10;
        if (operation.equals("÷")){
            if (firstNum > secondNum) {
                while (secondNum == 0) {
                    if (userScore < 1000) {
                        secondNum = (int) (Math.random() * 10);
                    } else if (userScore < 1500) {
                        if (multiOrDivide <= 5) {
                            secondNum = (int) (Math.random() * 100);
                        } else {
                            secondNum = (int) (Math.random() * 10);
                        }
                    } else if (userScore < 1700) {
                        secondNum = (int) (Math.random() * 100);
                    } else {
                        if (multiOrDivide <= 5) {
                            secondNum = (int) (Math.random() * 100);
                        } else {
                            secondNum = (int) (Math.random() * 10);
                        }
                    }
                }
                while (firstNum % secondNum != 0) {
                    if (userScore < 1000) {
                        secondNum = (int) (Math.random() * 10);
                        if (secondNum == 0) {
                            while (secondNum == 0) {
                                secondNum = (int) (Math.random() * 10);
                            }
                        }
                    } else if (userScore < 1500) {
                        if (multiOrDivide <= 5) {
                            secondNum = (int) (Math.random() * 100);
                            if (secondNum == 0) {
                                while (secondNum == 0) {
                                    secondNum = (int) (Math.random() * 100);
                                }
                            }
                        } else {
                            secondNum = (int) (Math.random() * 10);
                            if (secondNum == 0) {
                                while (secondNum == 0) {
                                    secondNum = (int) (Math.random() * 10);
                                }
                            }
                        }
                    } else if (userScore < 1700) {
                        secondNum = (int) (Math.random() * 100);
                        if (secondNum == 0) {
                            while (secondNum == 0) {
                                secondNum = (int) (Math.random() * 100);
                            }
                        }
                    } else {
                        if (multiOrDivide <= 5) {
                            secondNum = (int) (Math.random() * 100);
                            if (secondNum == 0) {
                                while (secondNum == 0) {
                                    secondNum = (int) (Math.random() * 100);
                                }
                            }
                        } else {
                            secondNum = (int) (Math.random() * 10);
                            if (secondNum == 0) {
                                while (secondNum == 0) {
                                    secondNum = (int) (Math.random() * 10);
                                }
                            }
                        }
                    }
                }
                correctAnswer = firstNum / secondNum;
            } else {
                while (firstNum == 0) {
                    if (userScore < 1000) {
                        firstNum = (int) (Math.random() * 10);
                    } else if (userScore < 1500) {
                        if (multiOrDivide <= 5) {
                            firstNum = (int) (Math.random() * 100);
                        } else {
                            firstNum = (int) (Math.random() * 10);
                        }
                    } else if (userScore < 1700) {
                        firstNum = (int) (Math.random() * 100);
                    } else {
                        if (multiOrDivide <= 5) {
                            firstNum = (int) (Math.random() * 100);
                        } else {
                            firstNum = (int) (Math.random() * 10);
                        }
                    }
                }
                while (secondNum % firstNum != 0) {
                    if (userScore < 1000) {
                        firstNum = (int) (Math.random() * 10);
                        if (firstNum == 0) {
                            while (firstNum == 0) {
                                firstNum = (int) (Math.random() * 10);
                            }
                        }
                    } else if (userScore < 1500) {
                        if (multiOrDivide <= 5) {
                            firstNum = (int) (Math.random() * 100);
                            if (firstNum == 0) {
                                while (firstNum == 0) {
                                    firstNum = (int) (Math.random() * 100);
                                }
                            }
                        } else {
                            firstNum = (int) (Math.random() * 10);
                            if (firstNum == 0) {
                                while (firstNum == 0) {
                                    firstNum = (int) (Math.random() * 10);
                                }
                            }
                        }
                    } else if (userScore < 1700) {
                        firstNum = (int) (Math.random() * 100);
                        if (firstNum == 0) {
                            while (firstNum == 0) {
                                firstNum = (int) (Math.random() * 100);
                            }
                        }
                    } else {
                        if (multiOrDivide <= 5) {
                            firstNum = (int) (Math.random() * 100);
                            if (firstNum == 0) {
                                while (firstNum == 0) {
                                    firstNum = (int) (Math.random() * 100);
                                }
                            }
                        } else {
                            firstNum = (int) (Math.random() * 10);
                            if (firstNum == 0) {
                                while (firstNum == 0) {
                                    firstNum = (int) (Math.random() * 10);
                                }
                            }
                        }
                    }
                }
                correctAnswer = secondNum / firstNum;
            }
        setFirstNum(firstNum);
        setSecondNum(secondNum);
    }
        return correctAnswer;
    }

    public int hardDivisionFixer(int firstNum, String operation, int secondNum, int userScore) {
        int correctAnswer = -1;
        double multiOrDivide = Math.random() * 10;
        if (operation.equals("÷")) {
            if (firstNum > secondNum) {
                while (secondNum == 0) {
                    if (userScore < 250) {
                        secondNum = (int) (Math.random() * 10);
                    } else if (userScore < 875) {
                        secondNum = (int) (Math.random() * 100);
                    } else {
                        if (multiOrDivide <= 5) {
                            secondNum = (int) (Math.random() * 100);
                        } else {
                            secondNum = (int) (Math.random() * 1000);
                        }
                    }
                }
                while (firstNum % secondNum != 0) {
                    if (userScore < 250) {
                        secondNum = (int) (Math.random() * 10);
                        if (secondNum == 0) {
                            while (secondNum == 0) {
                                secondNum = (int) (Math.random() * 10);
                            }
                        }
                    } else if (userScore < 875) {
                        secondNum = (int) (Math.random() * 100);
                        if (secondNum == 0) {
                            while (secondNum == 0) {
                                secondNum = (int) (Math.random() * 100);
                            }
                        }
                    } else {
                        if (multiOrDivide <= 5) {
                            secondNum = (int) (Math.random() * 100);
                            if (secondNum == 0) {
                                while (secondNum == 0) {
                                    secondNum = (int) (Math.random() * 100);
                                }
                            }
                        } else {
                            secondNum = (int) (Math.random() * 1000);
                            if (secondNum == 0) {
                                while (secondNum == 0) {
                                    secondNum = (int) (Math.random() * 1000);
                                }
                            }
                        }
                    }
                }
                correctAnswer = firstNum / secondNum;
                setFirstNum(firstNum);
                setSecondNum(secondNum);
            } else {
                while (firstNum == 0) {
                    if (userScore < 250) {
                        firstNum = (int) (Math.random() * 10);
                    } else if (userScore < 875) {
                        firstNum = (int) (Math.random() * 100);
                    } else {
                        if (multiOrDivide <= 5) {
                            firstNum = (int) (Math.random() * 100);
                        } else {
                            firstNum = (int) (Math.random() * 1000);
                        }
                    }
                }
                while (secondNum % firstNum != 0) {
                    if (userScore < 250) {
                        firstNum = (int) (Math.random() * 10);
                        if (firstNum == 0) {
                            while (firstNum == 0) {
                                firstNum = (int) (Math.random() * 10);
                            }
                        }
                    } else if (userScore < 875) {
                        firstNum = (int) (Math.random() * 100);
                        if (firstNum == 0) {
                            while (firstNum == 0) {
                                firstNum = (int) (Math.random() * 100);
                            }
                        }
                    } else {
                        if (multiOrDivide <= 5) {
                            firstNum = (int) (Math.random() * 100);
                            if (firstNum == 0) {
                                while (firstNum == 0) {
                                    firstNum = (int) (Math.random() * 100);
                                }
                            }
                        } else {
                            firstNum = (int) (Math.random() * 1000);
                            if (firstNum == 0) {
                                while (firstNum == 0) {
                                    firstNum = (int) (Math.random() * 1000);
                                }
                            }
                        }
                    }
                }
                correctAnswer = secondNum / firstNum;
                setFirstNum(firstNum);
                setSecondNum(secondNum);
            }
        }
        return correctAnswer;
    }

    public int streakDivisionFixer(int firstNum, String operation, int secondNum, int streakNum){
        int correctAnswer = -1;
        double multiOrDivide = Math.random() * 10;
        if (operation.equals("÷")) {
            if (firstNum > secondNum) {
                while (secondNum == 0) {
                    if (streakNum <= 10) {
                        secondNum = (int) (Math.random() * 10);
                    } else if (streakNum <= 18) {
                        secondNum = (int) (Math.random() * 100);
                    } else if (streakNum <= 20) {
                        secondNum = (int) (Math.random() * 1000);
                    } else {
                        if (multiOrDivide <= 5) {
                            secondNum = (int) (Math.random() * 100);
                        } else {
                            secondNum = (int) (Math.random() * 1000);
                        }
                    }
                }
                while (firstNum % secondNum != 0) {
                    if (streakNum <= 10) {
                        secondNum = (int) (Math.random() * 10);
                        if (secondNum == 0) {
                            while (secondNum == 0) {
                                secondNum = (int) (Math.random() * 10);
                            }
                        }
                    } else if (streakNum <= 18) {
                        secondNum = (int) (Math.random() * 100);
                        if (secondNum == 0) {
                            while (secondNum == 0) {
                                secondNum = (int) (Math.random() * 100);
                            }
                        }
                    } else if (streakNum <= 20) {
                        secondNum = (int) (Math.random() * 1000);
                        if (secondNum == 0) {
                            while (secondNum == 0) {
                                secondNum = (int) (Math.random() * 1000);
                            }
                        }
                    } else {
                        if (multiOrDivide <= 5) {
                            secondNum = (int) (Math.random() * 100);
                            if (secondNum == 0) {
                                while (secondNum == 0) {
                                    secondNum = (int) (Math.random() * 100);
                                }
                            }
                        } else {
                            secondNum = (int) (Math.random() * 1000);
                            if (secondNum == 0) {
                                while (secondNum == 0) {
                                    secondNum = (int) (Math.random() * 1000);
                                }
                            }
                        }
                    }
                }
                correctAnswer = firstNum / secondNum;
                setFirstNum(firstNum);
                setSecondNum(secondNum);
            } else {
                while (firstNum == 0) {
                    if (streakNum <= 10) {
                        firstNum = (int) (Math.random() * 10);
                    } else if (streakNum <= 18) {
                        firstNum = (int) (Math.random() * 100);
                    } else if (streakNum <= 20) {
                        firstNum = (int) (Math.random() * 1000);
                    } else {
                        if (multiOrDivide <= 5) {
                            firstNum = (int) (Math.random() * 100);
                        } else {
                            firstNum = (int) (Math.random() * 1000);
                        }
                    }
                }
                while (secondNum % firstNum != 0) {
                    if (streakNum <= 10) {
                        firstNum = (int) (Math.random() * 10);
                        if (firstNum == 0) {
                            while (firstNum == 0) {
                                firstNum = (int) (Math.random() * 10);
                            }
                        }
                    } else if (streakNum <= 18) {
                        firstNum = (int) (Math.random() * 100);
                        if (firstNum == 0) {
                            while (firstNum == 0) {
                                firstNum = (int) (Math.random() * 100);
                            }
                        }
                    } else if (streakNum <= 20) {
                        firstNum = (int) (Math.random() * 1000);
                        if (firstNum == 0) {
                            while (firstNum == 0) {
                                firstNum = (int) (Math.random() * 1000);
                            }
                        }
                    } else {
                        if (multiOrDivide <= 5) {
                            firstNum = (int) (Math.random() * 100);
                            if (firstNum == 0) {
                                while (firstNum == 0) {
                                    firstNum = (int) (Math.random() * 100);
                                }
                            }
                        } else {
                            firstNum = (int) (Math.random() * 1000);
                            if (firstNum == 0) {
                                while (firstNum == 0) {
                                    firstNum = (int) (Math.random() * 1000);
                                }
                            }
                        }
                    }
                }
                correctAnswer = secondNum / firstNum;
                setFirstNum(firstNum);
                setSecondNum(secondNum);
            }
        }
        return correctAnswer;
    }
}