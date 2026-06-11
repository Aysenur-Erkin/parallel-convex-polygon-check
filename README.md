# Paralel Konveks Poligon Kontrolü
Program Java diliyle yazılmıştır. Aynı problem önce seri olarak çözülmüş, daha sonra Java `Thread` yapısı kullanılarak paralel hale getirilmiştir. Amaç, seri ve paralel çalışma sürelerini karşılaştırmak ve hızlanma katsayısını görmektir.

## Projenin Konusu

Elimizde sıralı noktalar vardır. Bu noktalar birleştiğinde bir poligon oluşur.

Bir poligon içe doğru çökme yapmıyorsa konveks kabul edilir. Eğer bir noktada dönüş yönü değişiyorsa poligon konveks değildir.

Bu kontrol için her adımda üç nokta alınır ve çapraz çarpım hesaplanır. Çapraz çarpım sonucunun işareti dönüş yönünü gösterir. Bütün dönüşler aynı yönde ise poligon konvekstir.

## Kullanılan Yöntem

Projede iki farklı çözüm vardır:

1. **Seri çözüm**  
   Noktalar tek tek gezilir. Bütün kontrol tek döngüyle yapılır.

2. **Paralel çözüm**  
   Nokta dizisi thread sayısına göre parçalara bölünür. Her thread kendi aralığını kontrol eder. Daha sonra threadlerin sonuçları birleştirilir.

## Kullanılan Java Yapıları

- `Thread`
- `start()`
- `join()`
- `System.nanoTime()`

Süre ölçümü için `System.nanoTime()` kullanılmıştır.

Hızlanma katsayısı şu şekilde hesaplanmıştır:

```text
Hızlanma = Seri Süre / Paralel Süre
```

Bu değer 1'den büyükse paralel çözüm seri çözüme göre daha hızlı çalışmıştır.


## Programı Çalıştırma

Önce Java dosyası derlenir:

```bash
javac src/ParalelKonveksKontrol.java
```

Daha sonra program çalıştırılır:

```bash
java -cp src ParalelKonveksKontrol
```

Thread sayısı verilerek de çalıştırılabilir:

```bash
java -cp src ParalelKonveksKontrol 2
java -cp src ParalelKonveksKontrol 4
java -cp src ParalelKonveksKontrol 8
```

## Deney Sonuçları

Program 1000, 10000, 100000, 500000 ve 1000000 nokta için çalıştırılmıştır.

1000000 nokta için örnek sonuçlar:

| Thread Sayısı | Seri Süre (ms) | Paralel Süre (ms) | Hızlanma |
|---|---:|---:|---:|
| 2 | 8.7592 | 4.6483 | 1.88 |
| 4 | 9.0865 | 19.9737 | 0.45 |
| 8 | 10.1696 | 3.9321 | 2.59 |

Küçük nokta sayılarında paralel çözüm her zaman daha hızlı çıkmamıştır. Bunun nedeni thread oluşturma ve yönetme maliyetidir. Veri sayısı büyüdüğünde paralel çözüm daha anlamlı hale gelmiştir.

## Sonuç

Bu projede konveks poligon kontrolü hem seri hem de paralel olarak yapılmıştır. Paralel çözümde Java Thread kullanılmıştır. Sonuçlara göre paralel programlama büyük veri boyutlarında avantaj sağlayabilir. Fakat küçük veri boyutlarında thread maliyeti nedeniyle her zaman hızlanma elde edilemeyebilir.
