public class ParalelKonveksKontrol {

    static class Nokta {
        double x;
        double y;

        Nokta(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    static class KontrolThread extends Thread {
        Nokta[] noktalar;
        int baslangic;
        int bitis;

        boolean parcaKonveks = true;
        int bulunanYon = 0;

        KontrolThread(Nokta[] noktalar, int baslangic, int bitis) {
            this.noktalar = noktalar;
            this.baslangic = baslangic;
            this.bitis = bitis;
        }

        public void run() {
            int n = noktalar.length;

            for (int i = baslangic; i < bitis; i++) {
                Nokta a = noktalar[i];
                Nokta b = noktalar[(i + 1) % n];
                Nokta c = noktalar[(i + 2) % n];

                double deger = caprazCarpim(a, b, c);

                if (deger > 0) {
                    if (bulunanYon == 0) {
                        bulunanYon = 1;
                    } else if (bulunanYon != 1) {
                        parcaKonveks = false;
                        return;
                    }
                } else if (deger < 0) {
                    if (bulunanYon == 0) {
                        bulunanYon = -1;
                    } else if (bulunanYon != -1) {
                        parcaKonveks = false;
                        return;
                    }
                }
            }
        }
    }

    static double caprazCarpim(Nokta a, Nokta b, Nokta c) {
        double x1 = b.x - a.x;
        double y1 = b.y - a.y;

        double x2 = c.x - b.x;
        double y2 = c.y - b.y;

        return x1 * y2 - y1 * x2;
    }

    static boolean seriKonveksMi(Nokta[] noktalar) {
        if (noktalar.length < 3) {
            return false;
        }

        int genelYon = 0;
        int n = noktalar.length;

        for (int i = 0; i < n; i++) {
            Nokta a = noktalar[i];
            Nokta b = noktalar[(i + 1) % n];
            Nokta c = noktalar[(i + 2) % n];

            double deger = caprazCarpim(a, b, c);

            if (deger > 0) {
                if (genelYon == 0) {
                    genelYon = 1;
                } else if (genelYon != 1) {
                    return false;
                }
            } else if (deger < 0) {
                if (genelYon == 0) {
                    genelYon = -1;
                } else if (genelYon != -1) {
                    return false;
                }
            }
        }

        return true;
    }

    static boolean paralelKonveksMi(Nokta[] noktalar, int threadSayisi) throws InterruptedException {
        if (noktalar.length < 3) {
            return false;
        }

        int n = noktalar.length;

        if (threadSayisi > n) {
            threadSayisi = n;
        }

        KontrolThread[] threadler = new KontrolThread[threadSayisi];

        int parcaBoyutu = n / threadSayisi;

        for (int i = 0; i < threadSayisi; i++) {
            int baslangic = i * parcaBoyutu;
            int bitis;

            if (i == threadSayisi - 1) {
                bitis = n;
            } else {
                bitis = baslangic + parcaBoyutu;
            }

            threadler[i] = new KontrolThread(noktalar, baslangic, bitis);
            threadler[i].start();
        }

        for (int i = 0; i < threadSayisi; i++) {
            threadler[i].join();
        }

        int genelYon = 0;

        for (int i = 0; i < threadSayisi; i++) {
            if (!threadler[i].parcaKonveks) {
                return false;
            }

            int yerelYon = threadler[i].bulunanYon;

            if (yerelYon != 0) {
                if (genelYon == 0) {
                    genelYon = yerelYon;
                } else if (genelYon != yerelYon) {
                    return false;
                }
            }
        }

        return true;
    }

    static Nokta[] duzgunCokgenOlustur(int noktaSayisi) {
        Nokta[] noktalar = new Nokta[noktaSayisi];

        double yaricap = 1000.0;

        for (int i = 0; i < noktaSayisi; i++) {
            double aci = 2.0 * Math.PI * i / noktaSayisi;

            double x = yaricap * Math.cos(aci);
            double y = yaricap * Math.sin(aci);

            noktalar[i] = new Nokta(x, y);
        }

        return noktalar;
    }

    static Nokta[] iceCokenCokgenOlustur() {
        Nokta[] noktalar = new Nokta[5];

        noktalar[0] = new Nokta(0, 0);
        noktalar[1] = new Nokta(6, 0);
        noktalar[2] = new Nokta(3, 1);
        noktalar[3] = new Nokta(6, 5);
        noktalar[4] = new Nokta(0, 5);

        return noktalar;
    }

    public static void main(String[] args) throws InterruptedException {

        int threadSayisi = 4;

        if (args.length > 0) {
            threadSayisi = Integer.parseInt(args[0]);
        }

        System.out.println("Paralel Konveks Poligon Kontrolu");
        System.out.println("Kullanilan thread sayisi: " + threadSayisi);
        System.out.println();

        Nokta[] ornek = iceCokenCokgenOlustur();

        System.out.println("Kucuk ornek test:");
        System.out.println("Seri sonuc    : " + seriKonveksMi(ornek));
        System.out.println("Paralel sonuc : " + paralelKonveksMi(ornek, threadSayisi));
        System.out.println();

        int[] noktaSayilari = {1000, 10000, 100000, 500000, 1000000};

        System.out.println("Nokta Sayisi\tSeri Sure(ms)\tParalel Sure(ms)\tHizlanma\tSonuc Esit Mi");

        for (int noktaSayisi : noktaSayilari) {
            Nokta[] noktalar = duzgunCokgenOlustur(noktaSayisi);

            long seriBaslangic = System.nanoTime();
            boolean seriSonuc = seriKonveksMi(noktalar);
            long seriBitis = System.nanoTime();

            long paralelBaslangic = System.nanoTime();
            boolean paralelSonuc = paralelKonveksMi(noktalar, threadSayisi);
            long paralelBitis = System.nanoTime();

            double seriSure = (seriBitis - seriBaslangic) / 1_000_000.0;
            double paralelSure = (paralelBitis - paralelBaslangic) / 1_000_000.0;

            double hizlanma = seriSure / paralelSure;

            System.out.printf(
                    "%d\t\t%.4f\t\t%.4f\t\t\t%.2f\t\t%b%n",
                    noktaSayisi,
                    seriSure,
                    paralelSure,
                    hizlanma,
                    seriSonuc == paralelSonuc
            );
        }
    }
}
