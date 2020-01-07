# Shopping List -esimerkkisovellus

Tämän projektin on tarkoitus esitellä Haaga-Helian [Ohjelmointi 2](https://opinto-opas.haaga-helia.fi/course_unit/SWD4TN033) -opintojaksolla opeteltavia web-teknologioita käytännössä.

Tähän dokumentaatioon sekä tässä samassa Git-repositoriossa sijaitsevaan valmiiseen koodiin perehtymällä saat peruskäsityksen yksinkertaistetun verkkosovelluksen toteuttamisesta Javalla, Servleteillä, JavaScriptillä sekä Ajax-teknologioilla. Esimerkkisovelluksesta on jätetty pois oikeassa verkkopalvelussa oleellisia ominaisuuksia, kuten käyttäjän tunnistautuminen, joiden toteuttamiseen voit perehtyä tämän esimerkin jälkeen. Voit halutessasi myös jatkokehittää tätä esimerkkisovellusta hyödyntämään oikeaa tietokantaa tai tukemaan useita samanaikaisia ostoslistoja.

## Sovelluksen osat

### Java-backend

Esimerkkisovelluksen Java-osuus koostuu kolmesta luokasta sekä palvelimen käynnistämiseksi tehdystä [Main](src/main/java/launch/Main.java)-luokasta:

[ShoppingListRestServlet](src/main/java/servlet/ShoppingListRestServlet.java)-luokka vastaa selaimelta tuleviin pyyntöihin seuraavien HTTP-metodeja vastaavien metodien avulla:
* `doGet` palauttaa ostoslistan sisällön JSON-muodossa
* `doPost` ottaa vastaan uuden `ShoppingListItem`-olion ja tallentaa sen
* `doDelete` poistaa annetulla `id`:llä varustetun `ShoppingListItem`-rivin palvelimelta

[ShoppingListItem](src/main/java/model/ShoppingListItem.java) on Model-luokka, joka mallintaa yksittäistä ostoslistan riviä, jolla on kaksi attribuuttia:
* `id` (long)
* `title` (String)

[ShoppingListItemDao](src/main/java/database/ShoppingListItemDao.java)-luokka esittää DAO-mallin mukaista tietokantaluokkaa joka vastaa tiedon välittämisestä tietokannan ja muiden Java-luokkien välillä. Esimerkkisovelluksen yksinkertaistamiseksi ja sen riippuvuuksien minimoimiseksi luokka on toteutettu pitämään tietovarastonaan yksinkertaista `ArrayList`-oliota, joka alustetaan aina palvelimen uudelleenkäynnistyksen yhteydessä. 

### Tomcat-palvelinohjelmisto

Servlet-pohjaiset sovellukset tarvitsevat aina jonkin suoritusympäristön, joka tällä esimerkkiprojektilla on nimeltään Tomcat. Tomcat ja muut sovelluksen riippuvuudet on helpointa asentaa Maven-työkalua käyttäen, jota varten projektista löytyy valmis "Project Object Model"-tiedosto eli [pom.xml](pom.xml).

Maven-projekti on rakennettu noudattaen Heroku-pilvialustan esimerkkiä ("Create a Java Web Application Using Embedded Tomcat")[https://devcenter.heroku.com/articles/create-a-java-web-application-using-embedded-tomcat].

### JavaScript front-end

Sovelluksen selainkäyttöliittymä koostuu kahdesta tiedostosta:

[app.js](src/main/webapp/js/app.js) sisältää kaiken JavaScript-toimintalogiikan `ShoppingListApp`-nimisessä luokassa. Luokan avulla on mahdollista näyttää kaikki ostoslistan rivit sekä lisätä ja poistaa rivejä yksi kerrallaan.

[index.html](src/main/webapp/index.html) sisältää sivun sovelluksen tarvitseman käyttöliittymän, joka koostuu rivien lisäämiseen käytettävästä lomakkeesta, ostoslistan esittävästä HTML-taulukosta sekä selaimessa näkymättömäksi jäävästä `template`-pohjasta, jota hyödynnetään JavaScript-puolella uusien ostoslistarivien renderöimiseksi.

Lisäksi sovelluksessa hyödynnetään [Sakura](https://unpkg.com/sakura.css/css/sakura.css)-nimistä avoimen lähdekoodin CSS-kirjastoa, joka valikoitui sovellukseen siksi, että se ei vaadi lainkaan luokkien tai id-attribuuttien määrittelemistä sivun HTML-rakenteeseen.


### Asentaminen omalle koneelle

Suorittaaksesi sovelluksen ja muokataksesi sitä omalla koneellasi sinun on tuotava projekti GitHubista omaan Eclipseesi. Tämän pitäisi olla suoraviivainen operaatio Eclipsen import-ominaisuuden avulla, jonka käyttämiseksi voit [katsoa videon](https://www.youtube.com/watch?v=hiij77tpDM4) tai [selata ohjeita](https://www.google.com/search?q=eclipse+clone+from+github).

Kun projekti on "kloonattu" ja Maven-työkalu on asentanut sen riippuvuudet, voit käynnistää back end -palvelimen suorittamalla tiedoston [`src/main/java/launch/Main.java`](src/main/java/launch/Main.java). Main-luokan tarkoitus on käynnistää Tomcat-palvelin [tämän tutoriaalin](https://devcenter.heroku.com/articles/create-a-java-web-application-using-embedded-tomcat) mukaisesti ja Main-luokan sisältöä ei tarvitse ymmärtää tämän oppimateriaalin seuraamiseksi.

Kun palvelin on käynnistynyt, ota siihen yhteys selaimellasi kirjoittamalla osoiteriville http://localhost:8080.

![Shopping List Demo App](documentation/assets/img/app.png)

Voit nyt kokeilla tekstirivien lisäämistä sekä poistamista käyttämällä ostoslistan yläpuolista tekstikenttää sekä rivien x-painikkeita.


## Esimerkkiprojektin JavaScript-osuus

Tässä esimerkkisovelluksessa ja siihen liittyvässä dokumentaatiossa oletetaan sekä JavaScript-kielen että siihen liittyvien kehitysympäristöjen olevan lukijalle jo jokseenkin tuttuja.

Esimerkki on pyritty rakentamaan siten, että siinä noudatetaan yleisesti hyviksi todettuja käytäntöjä esimerkiksi koodin nimeämisessä sekä jäsentämisessä luokkiin ja metodeihin. Ero ohjelmointityylissä onkin pyritty tekemään mahdollisimman pieneksisovellettujen Java- ja JavaScript käytäntöjen välillä.

JavaScript-koodissa ohjelman rakenne saattaa usein muuttua melkoiseksi spagetiksi, jossa yksittäiset metodit pitävät sisällään niin tietoliikenteeseen kuin HTML-rakenteen käsittelyyn liittyviä toimenpiteitä. Tässä esimerkissä pyritään jakamaan ohjelma tarkasti erillisiin osiin, vaikka se yksittäisiä esimerkkejä hieman monimutkaistaisikin.

### Nuolifunktiot

JavaScript-sovelluksen ([src/main/webapp/js/app.js](src/main/webapp/js/app.js)) lähdekoodissa esiintyy JavaScript-maailmassa yleistyviä nuolifunktioita, esim: `(a, b) => a + b`. Tämä nuolifunktio olisi  lyhyempi syntaksi perinteisille anonyymeille funktioille:

```javascript
function(a, b) {
    return a + b;
}
```

Nuolifunktioiden ja tavallisten anonyymien funktioiden syntaksin lisäksi myös niiden toiminnassa on kuitenkin tärkeitä eroja:

>The handling of this is also different in arrow functions compared to regular functions.
>
>In short, with arrow functions there are no binding of this.
>
>In regular functions the this keyword represented the object that called the function, which could be the window, the document, a button or whatever.
>
>With arrow functions the this keyword always represents the object that defined the arrow function.
>
> *[W3Schools](https://www.w3schools.com/js/js_arrow_function.asp)*

Käytännössä edellä esitetty W3Schools:in selitys tarkoittaa sitä, että kahdesta tätä sovellusta varten koodatuista `onclick`-tapahtumakuuntelijoista vain jälkimmäinen oikeasti toimii:

```javascript
// perinteinen anonyymi funktio 
removeButton.onclick = function () {
    this.deleteItem(item);
};

// nuolifunktio
removeButton.onclick = () => {
    this.deleteItem(item);
};
```

Perinteiseen tapaan toteutetussa ylemmässä tapahtumankuuntelijassa esiintyvä `this` ei funktiota suoritettaessa viittaakaan enää siihen `ShoppingListApp`-olioon, jonka sisällä se on määritetty, vaan siihen painikkeeseen, jota klikattiin.

Alemmassa nuolifunktiolla toteutetussa versiossa 
`this`-muuttuja viittaa aina siihen olioon, jonka metodissa tapahtumakuuntelija asetettiin. Näin funktio on toteutettu myös [tämän projektin lähdekoodissa](src/main/webapp/js/app.js).

[Kokeile esimerkkiä Codepen.io](https://codepen.io/h01581/pen/rNaJaEN):ssa.

### Ajax

Termi "Ajax" on lyhenne sanoista "Asynchronous JavaScript and XML". Nykyään tiedostonsiirtoon käytetään XML-formaatin sijaan yksinkertaisempaa JSON-formaattia. 

Ajax-teknologioita hyödynnetään tässä projektissa ostoslistan sisällön päivittämisessä dynaamisesti siten, että selain hakee taustalla dataa palvelimelta ja näyttää sen ilman erillistä sivulatausta. Vastaavasti tietojen lisääminen ja poistaminen eivät edellytä sivulatausta, vaan tieto siirtyy taustalla ja päivittyy sivulle dynaamisesti.


#### Fetch, jQuery, XMLHttpRequest...

Tämän esimerkkiprojektin tiedonsiirto on toteutettu hyödyntäen JavaScriptin omaa [fetch](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API)-funktiota sekä `async/await`-ohjelmointityyliä. Vaihtoehtoisia toteutusteknologioita tiedonsiirrolle olisivat esimerkiksi [jQuery](https://jquery.com/)-kirjasto tai JavaScriptin vanhempi [XMLHttpRequest](https://developer.mozilla.org/en-US/docs/Web/API/XMLHttpRequest)-luokka, joita ei tässä materiaalissa käsitellä tarkemmin.

Erillisiä kirjastoja välttämällä opit kirjoittamaan koodiasi yleisemmällä tasolla ja voit hyödyntää taitojasi myös nettisivujen ulkopuolella, esimerkiksi Node.js-sovelluksissa ja mobiilisovelluksissa.

Aivan viimeisimpien teknologioiden hyödyntämiseen verkkosovelluksissa liittyy varjopuolia, kuten vaihteleva yhteensopivuus eri selainten ja erityisesti niiden vanhempien versioiden kanssa. Kirjoitushetkellä tässä materiaalissa hyödynnetyt selainteknologiat ovat tuettuja kaikilla moderneilla selaimilla (ks: [nuolifunktiot](https://caniuse.com/#feat=arrow-functions), [fetch](https://caniuse.com/#feat=fetch), [async/await](https://caniuse.com/#feat=async-functions), [template](https://caniuse.com/#feat=template)). 

Lukiessasi tätä materiaalia tänään, voit olla huojentunut siitä, että vanhentuneiden selainversioiden käyttäjämäärät ehtivät vielä jonkin aikaa laskea ennen kuin kirjoitat tuotantokoodia suurelle yleisölle. Monissa tapauksissa nykyaikaisesti kirjoitettu koodi on myös automaattisesti käännettävissä vanhempien selainversioiden ymmärtämään muotoon esimerkiksi [Babel-kääntäjällä](https://babeljs.io/).


### Asynkronisuus, Callback, Promise ja Async/Await

Ajax-teknologioiden asynkronisuus johtuu siitä, että JavaScript suoritetaan vain yhdessä säikeessä, jossa suoritetaan kerrallaan vain yhtä lauseketta. Jos esimerkiksi tiedonsiirto tehtäisiin synkronisesti, jumittuisi koko JavaScript-sovellus siksi aikaa, kunnes tiedonsiirto valmistuu. Voit lukea lisää asynkronisesta ohjelmoinnista esimerkiksi ["Understanding Asynchronous JavaScript"](https://blog.bitsrc.io/understanding-asynchronous-javascript-the-event-loop-74cd408419ff)-artikkelista ja Mozillan ["Asynchronous JavaScript"](https://developer.mozilla.org/en-US/docs/Learn/JavaScript/Asynchronous)-oppimateriaalista.

Hitaiden kutsujen ongelma on ratkaistu JavaScript-maailmassa antamalla "hitaalle koodille" funktio, joka sen tulee suorittaa operaation valmistuttua. Tällaisia funktioita kutsutaan ns. callback-funktioiksi. 

> Callback on oikeastaan tapahtumankäsittelijä, jonka "tapahtuma" on "palvelupyynnön valmistuminen"!
> 
> *Tommi Tuura, https://www.cs.helsinki.fi/u/ttuura/otk-js/asynkronisuus.html*

Kun koodissa on tarpeen tehdä lukuisia perkkäisiä hitaita operaatioita, syntyy helposti syviä sisäkkäisiä rakenteita, joissa callback-funktiot kutsuvat uusia hitaita operaatioita ja antavat jälleen parametreina uusia callback-funktioita:

```javascript
doSomething(function(result) {
  doSomethingElse(result, function(newResult) {
    doThirdThing(newResult, function(finalResult) {
      console.log('Got the final result: ' + finalResult);
    }, failureCallback);
  }, failureCallback);
}, failureCallback);

// source: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Using_promises
```

Syvien sisäkkäisten rakenteiden välttämiseksi asynkronisten funktioiden toteutustavaksi on vakiintunut myös JavaSciptin spesifikaatioon lisätty [Promise](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Promise)-luokka, jonka avulla useita asynkronisia kutsuja saadaan kätevästi ketjutettua. 

Esimerkkikoodin `app.js`-tiedostossa peräkkäiset asynkroniset `fetch`- ja `json`-kutsut palauttavat `Promise`-oliota. `Promise`-olion tapahtumankuuntelija asetetaan kutsumalla `Promise`n `then`-metodia ja antamalla sille callback-funktio. Peräkkäisiä `Promise`-oliota voidaan myös ketjuttaa seuraavasti, jolloin ensimmäisenä `Promise`n `then`-metodille annettu funktio suoritetaan aina ennen seuraavia kutsuja:

```javascript
fetch("/api/shoppingList/items")
    .then((response) => response.json())
    .then((json) => this._items = json)
    .then(() => this._render())
```

Then-kutsujen ketjuttaminen aiheuttaa kuitenkin edelleen haasteitaan koodin luettavuudelle. Sama koodi voidaan kirjoittaa yksinkertaisemmalla tavalla siten, että se hyödyntää `Promise`-toimintamallia, mutta näyttää ulkoisesti synkroniselta. Tämä tapahtuu hyödyntäen JavaScriptin `await`-avainsanaa:

```javascript
let response = await fetch("/api/shoppingList/items");
this._items = await response.json();
this._render();
```
*[src/main/webapp/js/app.js](src/main/webapp/js/app.js)*

Nykyaikaiset JavaScript-tulkit osaavat kääntää `await`-avainsanalla merkityt rivit siten, että koodi sisennetään `Promise`:n `then`-ketjuiksi automaattisesti taustalla, kunhan `await`-kutsuja sisältävän funktion määrittelyn alkuun on kirjoitettu avainsana `async`, esim seuraavasti: 

```javascript
async deleteItem(deleted) {
    let response = await fetch(
        `/api/shoppingList/items?id=${deleted.id}`,
        { method: 'DELETE' }
    );
    this._items = this._items.filter(item => item !== deleted);
    this._render();
}
```
*[src/main/webapp/js/app.js](src/main/webapp/js/app.js)*

⚠️ Mikäli kehittämääsi verkkosovellusta on tarkoitus käyttää myös vanhemmilla selainversioilla, jotka eivät osaa tulkita `await`-rakenteita, voidaan kääntäminen tehdä myös valmiiksi ennen sovelluksen julkaisua [Babel-kääntäjällä](https://babeljs.io/).


### JSON (JavaScript Object Notation)

Tiedonsiirtoformaattina tämän esimerkkisovelluksen selaimessa toimivan JavaScript-koodin ja palvelimella toimivan Java-koodin välillä on JSON. 

> JSON is a text format that is completely language independent but uses conventions that are familiar to programmers of the C-family of languages, including C, C++, C#, Java, JavaScript, Perl, Python, and many others. These properties make JSON an ideal data-interchange language.
> 
> *https://www.json.org/json-en.html*

Kun sovelluksen pääsivu `[index.html](src/main/webapp/index.html)` avataan, lataa JS-sovellus taustalla ostoslistan senhetkisen sisällön osoitteesta `/api/shoppingList/items`. Vastauksena selain vastaanottaa JSON-dokumentin, jonka sisältö on muodoltaan seuraavanlainen:

```json
[
  {
    "id":1,
    "title":"Milk"
  },
  {
    "id":2,
    "title":"Eggs"
  },
  {
    "id":3,
    "title":"Bread"
  }
]
```

Vastaus koostuu siis taulukosta `[]`, jonka sisällä on tässä esimerkissä kolme oliota (`{}`). Kullakin oliolla on kaksi attribuuttia: `id` ja `title`, jotka ovat tyypeiltään numero ja merkkijono. Nämä attribuutit vastaavat suoraan palvelinpäässä määritellyn [`ShoppingListItem`](src/main/java/model/ShoppingListItem.java)-luokan oliomuuttujia:

```java
public class ShoppingListItem {

    private int id;
    private String title;

    public ShoppingListItem(int id, String title) {
        this.id = id;
        this.title = title;
    }

    // ...
}
```

#### JavaScript ↔️ Java ↔️ JavaScript
JSON-tiedostomuoto sopii erinomaisesti eri ohjelmointikielien väliseen tiedonvälitykseen ja eri kielillä toteutetut oliot on muutettavissa toisen kielen olioksi parhaassa tapauksessa automaattisesti. JSON-muunnoksia varten tässä esimerkkiprojektissa hydynnetään Googlen kehittämää [Gson-kirjastoa](https://github.com/google/gson). Gson-kirjasto ei ole osa Javan standardikirjastoa, vaan se on lisätty projektiin Maven-työkalun avulla määrittelemällä se [pom.xml](pom.xml)-tiedostoon.

Edellä esitetty JSON-muotoinen esitys ostoslistan sisällöstä generoidaan palvelimella [`ShoppingListRestServlet`](src/main/java/servlet/ShoppingListRestServlet.java)-luokan `doGet`-metodissa seuraavasti:

```java
List<ShoppingListItem> allItems = dao.getAllItems();

// convert the Java objects into a JSON formatted String:
String json = new Gson().toJson(allItems);
```

Vastaavasti samassa luokassa muodostetaan JavaScript-sovelluksen lähettämästä JSON-merkkijonosta Java-olio `doPost`-metodissa seuraavasti:

```java
// read all lines from the POST request body and join them into one String:
String jsonString = req.getReader().lines().collect(Collectors.joining());

// convert the read JSON input from a String into a ShoppingListItem object:
ShoppingListItem newItem = new Gson().fromJson(jsonString, ShoppingListItem.class);

```

`Gson`-kirjasto luo yllä uuden olion automaattisesti käyttäen sille antamamme `ShoppingListItem`-luokan parametritonta konstruktoria, minkä jälkeen se asettaa JSON-rakenteessa olevat arvot olion samannimisiin muuttujiin.

JavaScript-puolella palvelimen tuottamat JSON-rakenteet ovat valmiiksi kielen tukemassa muodossa, joten muunnosta ei JS-koodissa tarvitse erikseen tehdä. `fetch`-kutsun palauttama [Response-olio](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch#Response_objects) antaa JSON:ia vastaavan JavaScript-olion promiseen käärittynä, kun kutsumme sen `json()`-metodia:

```javascript
let response = await fetch("/api/shoppingList/items");
this._items = await response.json();
this._render();
```
*[app.js](src/main/webapp/js/app.js)*




## Datan näyttäminen sivulla

### `<template>`-tagi
https://developer.mozilla.org/en-US/docs/Web/HTML/Element/template

### Moustache, Pug, jQuery



## Osallistu tämän materiaalin kehittämiseen

Tämä dokumentaatio on kirjoitettu [markdown](https://guides.github.com/features/mastering-markdown/)-syntaksilla ja sitä ylläpidetään ohjelmistokehittäjien parissa erittäin suositussa GitHub-palvelussa. Voit [esittää kysymyksiä ja kehitysideoita]() sekä tehdä [muutosehdotuksia materiaaliin sekä palvelun lähdekoodeihin]() GitHubissa.

## Lisenssi

Tämä oppimateriaali on lisensoitu [Creative Commons BY-NC-SA 3.0](https://creativecommons.org/licenses/by-nc-sa/3.0/) -lisenssillä. 
