/*
THIS GAME IS MADE BY NIKHIL
*/
import java.util.*;

public class mafiaGame {
    Set<Integer> alivesplyrs = new HashSet<>();
    Set<Integer> mafiavoteingplyrs = new HashSet<>();
    Set<Integer> mafiaplyrs = new HashSet<>();
    Set<Integer> detectiveTestingplyrs = new HashSet<>();
    Set<Integer> detectiveplyrs = new HashSet<>();

    public static void main(String[] args) {
        // INITIALISING all objects
        Scanner sc = new Scanner(System.in);
        mafiaGame game = new mafiaGame();

        double num, option; // Numbers of players/option selected
        int mafiaNum, healerNum, detectiveNum, commonerNum; // Numbers of mafia/healer/detective/commoner

        // Game starts
        System.out.println("_______Welcome to Mafia_______\n");
        System.out.print("Enter Number of players: ");

        // TO Handle to invalid values of number of players
        num = sc.nextDouble();
        while (num < 6 || num != (int) num) {
            System.out.println("\nPlease Enter a valid number (Must be an integer and greater than 5) ");
            System.out.print("Enter Number of players: ");
            num = sc.nextDouble();
        }

        // Selecting option and handling invalid exception
        game.printMenu();
        option = sc.nextDouble();
        while (option < 1 || option > 5 || option != (int) option) {
            System.out.println("Please Select a valid option");
            option = sc.nextDouble();
        }

        // Number of all types of players
        mafiaNum = (int) num / 5;
        detectiveNum = (int) num / 5;
        healerNum = Math.max(1, (int) num / 10);
        commonerNum = (int) num - (mafiaNum + detectiveNum + healerNum);

        // Initialising all players
        int arr[] = new int[(int) num];
        game.generateRandom((int) num, arr);
        playersList<mafia> mafialist = new playersList<mafia>();
        playersList<detective> detectivelist = new playersList<detective>();
        playersList<healer> healerlist = new playersList<healer>();
        playersList<commoner> commonerlist = new playersList<commoner>();

        int pointer = 0;
        for (int i = 0; i < mafiaNum; i++, pointer++) {
            mafialist.add(arr[pointer], new mafia(arr[pointer]));
            game.alivesplyrs.add(arr[pointer]);
            game.detectiveTestingplyrs.add(arr[pointer]);
            game.mafiaplyrs.add(arr[pointer]);
        }
        for (int i = 0; i < detectiveNum; i++, pointer++) {
            detectivelist.add(arr[pointer], new detective(arr[pointer]));
            game.alivesplyrs.add(arr[pointer]);
            game.mafiavoteingplyrs.add(arr[pointer]);
            game.detectiveplyrs.add(arr[pointer]);
        }
        for (int i = 0; i < healerNum; i++, pointer++) {
            healerlist.add(arr[pointer], new healer(arr[pointer]));
            game.alivesplyrs.add(arr[pointer]);
            game.mafiavoteingplyrs.add(arr[pointer]);
            game.detectiveTestingplyrs.add(arr[pointer]);
        }
        for (int i = 0; i < commonerNum; i++, pointer++) {
            commonerlist.add(arr[pointer], new commoner(arr[pointer]));
            game.alivesplyrs.add(arr[pointer]);
            game.mafiavoteingplyrs.add(arr[pointer]);
            game.detectiveTestingplyrs.add(arr[pointer]);
        }
        // initialising user object
        players user;
        if (option == 1) {
            user = mafialist.list.get(arr[0]);
        } else if (option == 2) {
            user = detectivelist.list.get(arr[mafiaNum]);
        } else if (option == 3) {
            user = healerlist.list.get(arr[detectiveNum + mafiaNum]);
        } else if (option == 4) {
            user = commonerlist.list.get(arr[mafiaNum + detectiveNum + healerNum]);
        } else {
            int rand = game.random(0, (int) (num - 1));
            if (rand >= 0 && rand < mafiaNum) {
                user = mafialist.list.get(arr[0]);
                option = 1;
            } else if (rand >= mafiaNum && rand < mafiaNum + detectiveNum) {
                user = detectivelist.list.get(arr[mafiaNum]);
                option = 2;
            } else if (rand >= mafiaNum && rand < mafiaNum + detectiveNum + healerNum) {
                user = healerlist.list.get(arr[detectiveNum + mafiaNum]);
                option = 3;
            } else {
                user = commonerlist.list.get(arr[mafiaNum + detectiveNum + healerNum]);
                option = 4;
            }
        }

        // remaining adjust
        String usertype = "";
        if (option == 1) {
            usertype = "mafia";
        } else if (option == 2) {
            usertype = "detective";
        } else if (option == 3) {
            usertype = "healer";
        } else {
            usertype = "commoner";
        }
        System.out.println("You are Player" + user.getIndex());
        System.out.print("You are a " + usertype);
        System.out.print(". Others " + usertype + "s are : ");

        if (option == 1) {
            mafialist.getall(user);
        } else if (option == 2) {
            detectivelist.getall(user);
        } else if (option == 3) {
            healerlist.getall(user);
        } else if (option == 4) {
            commonerlist.getall(user);
        }

        System.out.println("\n");
        // GAME STARTS
        int rounds = 1;
        while (game.stopper1(mafialist, detectivelist, healerlist, commonerlist) && game.stopper2(mafialist)) {
            System.out.println("Round " + rounds + " :");
            System.out.print(game.alivesplyrs.size() + " players are remaining: ");
            for (int i : game.alivesplyrs) {
                System.out.print("Player" + i + " ");
            }
            System.out.println("are alive");
            // MAFIA PROCESS
            double mafiatarget = -1;
            if (mafialist.getSize() != 0) {
                if (user.isAlive() && option == 1) {
                    System.out.print("Choose a target: ");
                    mafiatarget = sc.nextDouble();
                    while (mafiatarget != (int) mafiatarget || !game.alivesplyrs.contains((int) mafiatarget)
                            || !game.mafiavoteingplyrs.contains((int) mafiatarget)) {
                        if (mafiatarget != (int) mafiatarget) {
                            System.out.println("Please Enter a valid input");
                        } else if (!game.alivesplyrs.contains((int) mafiatarget)) {
                            System.out.println("Player" + (int) mafiatarget + " is not in the game.");
                        } else if (game.mafiaplyrs.contains((int) mafiatarget)) {
                            System.out.println("You cannot choose a mafia.");
                        }
                        System.out.print("Choose a target: ");
                        mafiatarget = sc.nextDouble();
                    }
                } else {
                    mafiatarget = game.randomFromSet(game.mafiavoteingplyrs);
                }
                game.mafiaProcess((int) mafiatarget, mafialist, detectivelist, healerlist, commonerlist);
            }

            if (option == 1) {
                if (!user.isAlive()) {
                    System.out.println("Mafias have chosen their target.");
                }
            } else {
                System.out.println("Mafias have chosen their target.");
            }

            // DETECTIVE PROCESS
            double detectivetarget = -1;
            boolean test = false;
            if (detectivelist.getSize() != 0) {
                if (user.isAlive() && option == 2) {
                    System.out.print("Choose a player to test: ");
                    detectivetarget = sc.nextDouble();
                    while (detectivetarget != (int) detectivetarget
                            || !game.detectiveTestingplyrs.contains((int) detectivetarget)
                            || !game.alivesplyrs.contains((int) detectivetarget)) {
                        if (detectivetarget != (int) detectivetarget) {
                            System.out.println("Please Enter a valid input");
                        } else if (!game.alivesplyrs.contains((int) detectivetarget)) {
                            System.out.println("Player" + (int) detectivetarget + " is not in the game.");
                        } else if (game.detectiveplyrs.contains((int) detectivetarget)) {
                            System.out.println("You cannot test a detective.");
                        }
                        System.out.print("Choose a player to test: ");
                        detectivetarget = sc.nextDouble();
                    }
                } else {
                    detectivetarget = game.randomFromSet(game.detectiveTestingplyrs);
                }
                test = game.detectiveProcess((int) detectivetarget, mafialist, detectivelist, healerlist, commonerlist);
            }

            if (option == 2) {
                if (user.isAlive()) {
                    if (test) {
                        System.out.println("Player" + (int) detectivetarget + " is a mafia");
                    } else {
                        System.out.println("Player" + (int) detectivetarget + " is not a mafia");
                    }
                } else {
                    System.out.println("Detectives have chosen a player to test.");
                }
            } else {
                System.out.println("Detectives have chosen a player to test.");
            }

            // HEALER PROCESS
            if (healerlist.getSize() != 0) {
                double healertarget;
                if (user.isAlive() && option == 3) {
                    System.out.print("Choose a player to heal: ");
                    healertarget = sc.nextDouble();
                    while (healertarget != (int) healertarget || !(game.alivesplyrs.contains((int) healertarget))) {
                        if (healertarget != (int) healertarget) {
                            System.out.println("Please Enter valid input");
                        } else {
                            System.out.println("Player" + (int) healertarget + " is not in the game.");
                        }
                        System.out.print("Choose a player to heal: ");
                        healertarget = sc.nextDouble();
                    }
                } else {
                    healertarget = game.randomFromSet(game.alivesplyrs);
                }
                game.healerProcess((int) healertarget, mafialist, detectivelist, healerlist, commonerlist);
            }

            if (option == 3) {
                if (!user.isAlive()) {
                    System.out.println("Healers have chosen someone to heal.");
                }
            } else {
                System.out.println("Healers have chosen someone to heal.");
            }

            System.out.println("--End of actions--");
            // dead person if any by mafias
            boolean mafiatargetdied = game.mafiatargetDied((int) mafiatarget, mafialist, detectivelist, healerlist,
                    commonerlist);
            if (mafiatargetdied) {
                game.deleteplyr((int) mafiatarget, mafialist, detectivelist, healerlist, commonerlist, game.alivesplyrs,
                        game.mafiaplyrs, game.mafiavoteingplyrs, game.detectiveTestingplyrs, game.detectiveplyrs);
                System.out.println("Player" + (int) mafiatarget + " has died");
            } else {
                System.out.println("No one died");
            }

            // voting ROUND
            if (test) {
                // code for kick out mafia detected player adn not voting round
                game.deleteplyr((int) detectivetarget, mafialist, detectivelist, healerlist, commonerlist,
                        game.alivesplyrs, game.mafiaplyrs, game.mafiavoteingplyrs, game.detectiveTestingplyrs,
                        game.detectiveplyrs);
                System.out.println("Player" + (int) detectivetarget + " has been voted out");
            } else {
                // Voting processs then kicking it out
                if (!game.stopper1(mafialist, detectivelist, healerlist, commonerlist)) {
                    break;
                }
                if (user.isAlive()) {
                    System.out.print("Select a person to vote out:");
                    double vote = sc.nextDouble();
                    while (vote != (int) vote || !game.alivesplyrs.contains((int) vote)) {
                        if (vote != (int) vote) {
                            System.out.println("Please Enter a valid input");
                        } else if (!game.alivesplyrs.contains((int) vote)) {
                            System.out.println("Player" + vote + " is not in the game.");
                        }
                        System.out.print("Select a person to vote out:");
                        vote = sc.nextDouble();
                    }
                }
                int kickout = game.randomFromSet(game.alivesplyrs);
                game.deleteplyr((int) kickout, mafialist, detectivelist, healerlist, commonerlist, game.alivesplyrs,
                        game.mafiaplyrs, game.mafiavoteingplyrs, game.detectiveTestingplyrs, game.detectiveplyrs);
                System.out.println("Player" + kickout + " has been voted out.");

            }
            System.out.println();
            rounds++;
        }

        System.out.println("Game Over");
        if (mafialist.getSize() == 0) {
            System.out.println("The Mafias have lost");
        } else {
            System.out.println("The Mafias have won");
        }
        System.out.println("\n");
        mafialist.getDetails(1, user);
        detectivelist.getDetails(2, user);
        healerlist.getDetails(3, user);
        commonerlist.getDetails(4, user);
        sc.close();
    }

    private void deleteplyr(int mafiatarget, playersList<mafia> mafialist, playersList<detective> detectivelist,
    playersList<healer> healerlist, playersList<commoner> commonerlist, Set<Integer> aalivesplyr, Set<Integer> mmafiaplyrs,
            Set<Integer> mmafiavoteingplyrs, Set<Integer> ddetectiveTestingplyrs, Set<Integer> ddetectiveplyrs) {
        players target;
        if (mafialist.containsKey(mafiatarget)) {
            target = (mafia) mafialist.list.get(mafiatarget);
            target.setAlive(false);
            mafialist.decSize();
            mmafiaplyrs.remove(mafiatarget);
            aalivesplyr.remove(mafiatarget);
            ddetectiveTestingplyrs.remove(mafiatarget);
        } else if (detectivelist.containsKey(mafiatarget)) {
            target = (detective) detectivelist.list.get(mafiatarget);
            target.setAlive(false);
            detectivelist.decSize();
            aalivesplyr.remove(mafiatarget);
            mmafiavoteingplyrs.remove(mafiatarget);
            ddetectiveplyrs.remove(mafiatarget);
        } else if (healerlist.containsKey(mafiatarget)) {
            target = (healer) healerlist.list.get(mafiatarget);
            target.setAlive(false);
            healerlist.decSize();
            aalivesplyr.remove(mafiatarget);
            mmafiavoteingplyrs.remove(mafiatarget);
            ddetectiveTestingplyrs.remove(mafiatarget);
        } else if (commonerlist.containsKey(mafiatarget)) {
            target = (commoner) commonerlist.list.get(mafiatarget);
            target.setAlive(false);
            commonerlist.decSize();
            aalivesplyr.remove(mafiatarget);
            mmafiavoteingplyrs.remove(mafiatarget);
            ddetectiveTestingplyrs.remove(mafiatarget);
        }
    }

    private boolean mafiatargetDied(int mafiatarget, playersList<mafia> mafialist, playersList<detective> detectivelist,
    playersList<healer> healerlist, playersList<commoner> commonerlist) {
        players target;
        if (mafialist.containsKey(mafiatarget)) {
            target = (mafia) mafialist.list.get(mafiatarget);
            return target.getHp() == 0;
        } else if (detectivelist.containsKey(mafiatarget)) {
            target = (detective) detectivelist.list.get(mafiatarget);
            return target.getHp() == 0;
        } else if (healerlist.containsKey(mafiatarget)) {
            target = (healer) healerlist.list.get(mafiatarget);
            return target.getHp() == 0;
        } else if (commonerlist.containsKey(mafiatarget)) {
            target = (commoner) commonerlist.list.get(mafiatarget);
            return target.getHp() == 0;
        }
        return false;
    }

    private void mafiaEliminationProcess(playersList<mafia> mafialist, double targetHp) {
        int eligiblemafia = mafialist.eligibleMafia();
        if (eligiblemafia == 0 || targetHp == 0) {
            return;
        }
        double factor = targetHp / eligiblemafia;
        double remainHp = mafialist.eliminateHp(factor);
        mafiaEliminationProcess(mafialist, remainHp);

    }

    private void mafiaProcess(int mafiatarget, playersList<mafia> mafialist, playersList<detective> detectivelist,
    playersList<healer> healerlist, playersList<commoner> commonerlist) {
        players target = null;
        if (mafialist.containsKey(mafiatarget)) {
            target = (mafia) mafialist.list.get(mafiatarget);
        } else if (detectivelist.containsKey(mafiatarget)) {
            target = (detective) detectivelist.list.get(mafiatarget);
        } else if (healerlist.containsKey(mafiatarget)) {
            target = (healer) healerlist.list.get(mafiatarget);
        } else if (commonerlist.containsKey(mafiatarget)) {
            target = (commoner) commonerlist.list.get(mafiatarget);
        }

        double mafiaTotolHp = mafialist.getAllMafiaHp();
        double targetHp = target.getHp();

        if (targetHp == mafiaTotolHp) {
            target.setHp(0);
            mafialist.nullAllmafiaHp();
        } else if (targetHp < mafiaTotolHp) {
            target.setHp(0);
            mafiaEliminationProcess(mafialist, targetHp);
        } else {
            target.addHp(-mafiaTotolHp);
            mafiaEliminationProcess(mafialist, targetHp);
        }
    }

    private boolean detectiveProcess(int detectivetarget, playersList<mafia> mafialist, playersList<detective> detectivelist,
            playersList<healer> healerlist, playersList<commoner> commonerlist) {
        players target;
        if (mafialist.containsKey(detectivetarget)) {
            target = (mafia) mafialist.list.get(detectivetarget);
            return target.isMafia();
        } else if (detectivelist.containsKey(detectivetarget)) {
            target = (detective) detectivelist.list.get(detectivetarget);
            return target.isMafia();
        } else if (healerlist.containsKey(detectivetarget)) {
            target = (healer) healerlist.list.get(detectivetarget);
            return target.isMafia();
        } else if (commonerlist.containsKey(detectivetarget)) {
            target = (commoner) commonerlist.list.get(detectivetarget);
            return target.isMafia();
        }
        return false;
    }

    private void healerProcess(int targetIndex, playersList<mafia> mafialist, playersList<detective> detectivelist,
    playersList<healer> healerlist, playersList<commoner> commonerlist) {
        players target;
        if (mafialist.containsKey(targetIndex)) {
            target = (mafia) mafialist.list.get(targetIndex);
            target.addHp(500);
        } else if (detectivelist.containsKey(targetIndex)) {
            target = (detective) detectivelist.list.get(targetIndex);
            target.addHp(500);
        } else if (healerlist.containsKey(targetIndex)) {
            target = (healer) healerlist.list.get(targetIndex);
            target.addHp(500);
        } else if (commonerlist.containsKey(targetIndex)) {
            target = (commoner) commonerlist.list.get(targetIndex);
            target.addHp(500);
        }

    }

    private int randomFromSet(Set<Integer> set) {
        int size = set.size();
        int item = new Random().nextInt(size);
        int i = 0;
        for (int obj : set) {
            if (i == item) {
                return obj;
            }
            i++;
        }
        return 0;
    }

    private void printMenu() {
        System.out.println(
                "Choose a Character\n  1) Mafia\n  2) Detective\n  3) Healer\n  4) Commoner\n  5) Assign Randomly");
    }

    private int random(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private boolean stopper2(playersList<mafia> mafialist) {
        if (mafialist.getSize() == 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean stopper1(playersList<mafia> mafialist, playersList<detective> detectivelist,
    playersList<healer> healerlist, playersList<commoner> commonerlist) {
        if (mafialist.getSize() < detectivelist.getSize() + healerlist.getSize() + commonerlist.getSize()) {
            return true;
        } else {
            return false;
        }
    }

    private int getNum(ArrayList<Integer> v) {
        int n = v.size();
        int index = (int) (Math.random() * n);
        int num = v.get(index);
        v.set(index, v.get(n - 1));
        v.remove(n - 1);
        return num;
    }

    void generateRandom(int n, int arr[]) {
        ArrayList<Integer> v = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            v.add(i + 1);
        }
        int i = 0;
        while (v.size() > 0) {
            arr[i] = getNum(v);
            i++;
        }
    }
}

abstract class players {
    private double hp;
    private boolean isAlive;

    abstract boolean isMafia();

    abstract boolean isDetective();

    abstract boolean isHealer();

    abstract boolean isCommoner();

    abstract int getIndex();

    public void setAlive(boolean alive) {
        this.isAlive = alive;
    }

    public boolean isAlive() {
        return isAlive;
    }

    double getHp() {
        return hp;
    }

    void setHp(double hp) {
        this.hp = hp;
    }

    void addHp(double hp1) {
        this.hp += hp1;
    }
}

class mafia extends players {
    private final int index;

    public mafia(int index) {
        this.index = index;
        setHp(2500);
        setAlive(true);
    }

    public int getIndex() {
        return index;
    }

    @Override
    boolean isMafia() {
        return true;
    }

    @Override
    boolean isDetective() {
        return false;
    }

    @Override
    boolean isHealer() {
        return false;
    }

    @Override
    boolean isCommoner() {
        return false;
    }
}

class detective extends players {
    private final int index;

    public detective(int index) {
        this.index = index;
        setHp(800);
        setAlive(true);
    }

    public int getIndex() {
        return index;
    }

    @Override
    boolean isMafia() {
        return false;
    }

    @Override
    boolean isDetective() {
        return true;
    }

    @Override
    boolean isHealer() {
        return false;
    }

    @Override
    boolean isCommoner() {
        return false;
    }
}

class healer extends players {
    private final int index;

    public healer(int index) {
        this.index = index;
        setHp(800);
        setAlive(true);
    }

    public int getIndex() {
        return index;
    }

    @Override
    boolean isMafia() {
        return false;
    }

    @Override
    boolean isDetective() {
        return false;
    }

    @Override
    boolean isHealer() {
        return true;
    }

    @Override
    boolean isCommoner() {
        return false;
    }
}

class commoner extends players {
    private final int index;

    public commoner(int index) {
        this.index = index;
        setHp(1000);
        setAlive(true);
    }

    public int getIndex() {
        return index;
    }

    @Override
    boolean isMafia() {
        return false;
    }

    @Override
    boolean isDetective() {
        return false;
    }

    @Override
    boolean isHealer() {
        return false;
    }

    @Override
    boolean isCommoner() {
        return true;
    }
}

class playersList<T> {
    private int size;
    Map<Integer, T> list;

    public playersList() {
        this.size = 0;
        list = new HashMap<>();
    }

    public int getSize() {
        return size;
    }

    public void incSize() {
        size += 1;
    }

    public void decSize() {
        size -= 1;
    }

    public void add(int index, T a) {
        list.put(index, a);
        incSize();
    }

    boolean containsKey(int key) {
        return list.containsKey(key);
    }

    // class NUmber 1:mafia, 2:detective, 3:healer, 4: commoner
    void getDetails(int classNumber, players user) {
        Set<Integer> key = list.keySet();
        for (int i : key) {
            if (classNumber == 1) {
                mafia obj = (mafia) list.get(i);
                if (compare(obj, user)) {
                    System.out.print("Players" + obj.getIndex() + "[user] ,");
                } else {
                    System.out.print("Players" + obj.getIndex() + " ,");
                }

            } else if (classNumber == 2) {
                detective obj = (detective) list.get(i);
                if (compare(obj, user)) {
                    System.out.print("Players" + obj.getIndex() + "[user] ,");
                } else {
                    System.out.print("Players" + obj.getIndex() + " ,");
                }

            } else if (classNumber == 3) {
                healer obj = (healer) list.get(i);
                if (compare(obj, user)) {
                    System.out.print("Players" + obj.getIndex() + "[user] ,");
                } else {
                    System.out.print("Players" + obj.getIndex() + " ,");
                }

            } else {
                commoner obj = (commoner) list.get(i);
                if (compare(obj, user)) {
                    System.out.print("Players" + obj.getIndex() + "[user] ,");
                } else {
                    System.out.print("Players" + obj.getIndex() + " ,");
                }

            }
        }
        if (classNumber == 1) {
            System.out.println(" were Mafias");
        } else if (classNumber == 2) {
            System.out.println(" were Detectives");
        } else if (classNumber == 3) {
            System.out.println(" were Healer");
        } else if (classNumber == 4) {
            System.out.println(" were Commoners");
        }
    }

    public double getAllMafiaHp() {
        double totalHp = 0;
        Set<Integer> key = list.keySet();
        for (int i : key) {
            mafia obj = (mafia) list.get(i);
            if (obj.isAlive()) {
                totalHp += obj.getHp();
            }
        }
        return totalHp;
    }

    public void nullAllmafiaHp() {
        Set<Integer> key = list.keySet();
        for (int i : key) {
            mafia obj = (mafia) list.get(i);
            if (obj.isAlive()) {
                obj.setHp(0);
            }
        }
    }

    public int eligibleMafia() {
        int num = 0;
        Set<Integer> key = list.keySet();
        for (int i : key) {
            mafia obj = (mafia) list.get(i);
            if (obj.isAlive() && obj.getHp() > 0) {
                num++;
            }
        }
        return num;
    }

    public double eliminateHp(double factor) {
        double ret = 0;
        Set<Integer> key = list.keySet();
        for (int i : key) {
            mafia obj = (mafia) list.get(i);
            if (obj.isAlive() && obj.getHp() > 0) {
                if (obj.getHp() >= factor) {
                    obj.addHp(-factor);
                } else {
                    ret += factor - obj.getHp();
                    obj.setHp(0);
                }
            }
        }
        return ret;
    }

    public void getall(players user) {
        Set<Integer> key = list.keySet();
        for (int i : key) {
            players obj = (players) list.get(i);
            if (!compare(obj, user)) {
                System.out.println("Player" + obj.getIndex());
            }
        }
    }

    private boolean compare(players a, players b) {
        if (a.getIndex() == b.getIndex()) {
            return true;
        } else {
            return false;
        }
    }
}
