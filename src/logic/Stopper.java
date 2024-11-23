package logic;

public class Stopper {
    private long startIdo;
    private long elteltIdo;
    private boolean fut;
    private Thread timeThread;

    public Stopper() {
        this.startIdo = 0;
        this.fut = false;
        this.elteltIdo = 0;
    }

    public void setKezdoIdo(long ido) {
        if (ido < 0 || ido > 3600000) { // Max. 1 óra
            elteltIdo = 0; // Hibás érték esetén alaphelyzet
        } else {
            elteltIdo = ido; // Érvényes érték beállítása
        }
    }

    // Indítja a stoppert és elkezdi az idő folyamatos frissítését
    public void start() {
        if (!fut) {
            // Az eddig eltelt idő hozzáadásának elkerülése
            startIdo = System.currentTimeMillis();
            fut = true;

            timeThread = new Thread(() -> {
                while (fut) {
                    try {
                        Thread.sleep(1000); // 1 másodperces késleltetés
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Az aktuális szál státuszának visszaállítása
                        break;
                    }
                }
            });
            timeThread.start();
        }
    }

    // Stopper megállítása
    public void stop() {
        if (fut) {
            elteltIdo += System.currentTimeMillis() - startIdo;
            fut = false;

            if (timeThread != null && timeThread.isAlive()) {
                try {
                    timeThread.interrupt(); // Megszakítás
                    timeThread.join(500);  // Max. 500 ms várakozás a szál befejezésére
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Aktuális szál helyreállítása
                }
            }
        }
    }

    // Eltelt idő visszaadása mm:ss.SSS formátumban
    public String getElteltIdo() {
        long totalTime = elteltIdo; // Összes idő az eltelt + aktuális futási idő
        if (fut) {
            totalTime += System.currentTimeMillis()-startIdo; // Ha fut, hozzáadjuk a futási időt
        }

        long mp = (totalTime / 1000) % 60;
        long min = (totalTime / 1000) / 60;
        long millis = totalTime % 1000;
        return String.format("%02d:%02d.%03d", min, mp, millis);
    }

    // Ellenőrzi, hogy a stopper fut-e
    public boolean isRunning() {
        return fut;
    }
    
    public void reset() {
        stop(); // Megállítjuk a stoppert
        elteltIdo = 0; // Az eltelt időt nullázzuk
        startIdo = 0; // A kezdő időt is nullázzuk
    }
}

