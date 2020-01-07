# Shopping List -esimerkkisovellus

Tämän projektin on tarkoitus esitellä Haaga-Helian [Ohjelmointi 2](https://opinto-opas.haaga-helia.fi/course_unit/SWD4TN033) -opintojaksolla opeteltavia web-teknologioita käytännössä.

Tähän dokumentaatioon sekä tässä samassa Git-repositoriossa sijaitsevaan valmiiseen koodiin perehtymällä saat peruskäsityksen yksinkertaistetun verkkosovelluksen toteuttamisesta Javalla, Servleteillä, JavaScriptillä sekä Ajax-teknologioilla. Valmiuksia tuotantokelpoisen, tehokkaan ja tietoturvallisen verkkosovelluksen kehittämiseen et vielä tätä projektia seuraamalla saa.

## Projektin palaset

Projektin Java-osuus on toteutettu Maven-työkalun avulla. Maven-työkalua käytettäessä projektin riippuvuudet, kuten TomCat-palvelinohjelmisto sekä [Gson-kirjasto](https://github.com/google/gson), määritellään [pom.xml](pom.xml)-tiedostoon, jonka avulla projekti on helppoa asentaa, kääntää ja pakata automaattisesti.

Maven-projektin rakenne noudattaa Heroku-pilvialustan esimerkkiä (Create a Java Web Application Using Embedded Tomcat)[https://devcenter.heroku.com/articles/create-a-java-web-application-using-embedded-tomcat].

### Projektin tiedostot
Java-tiedostot
index.html
app.js

## Asentaminen omalle koneelle

Eclipsen Git-kloonaus. TODO: project-tiedostot? linkki: https://www.omnijava.com/2016/07/10/importing-maven-projects-from-git-into-eclipse-that-were-created-by-netbeans/

Kun tiedostot on kloonattu ja projektin riippuvuudet asennettu Maven-työkalulla, voit käynnistää back end -palvelimen suorittamalla tiedoston [`src/main/java/launch/Main.java`](src/main/java/launch/Main.java). Main-luokan tarkoitus on käynnistää Tomcat-palvelin [tämän tutoriaalin](https://devcenter.heroku.com/articles/create-a-java-web-application-using-embedded-tomcat) mukaisesti ja luokan sisältöä ei tarvitse ymmärtää tämän oppimateriaalin seuraamiseksi.

Kun palvelin on käynnistynyt, ota siihen yhteys selaimellasi kirjoittamalla osoiteriville http://localhost:8080.

![Shopping List Demo App](documentation/assets/img/app.png)

Voit nyt kokeilla tekstirivien lisäämistä sekä poistamista käyttämällä ostoslistan yläpuolista tekstikenttää sekä rivien x-painikkeita.

## Esimerkkiprojektin JavaScript-osuus

Tässä esimerkkisovelluksessa ja siihen liittyvässä dokumentaatiossa oletetaan sekä JavaScript-kielen että siihen liittyvien kehitysympäristöjen olevan lukijalle jo jokseenkin tuttuja.

Esimerkki on pyritty rakentamaan siten, että siinä noudatetaan yleisesti hyviksi todettuja käytäntöjä esimerkiksi koodin nimeämisessä sekä jäsentämisessä luokkiin ja metodeihin. Ero ohjelmointityylissä on pyritty tekemään mahdollisimman pieneksisovellettujen Java- ja JavaScript käytäntöjen välillä.

### Nuolifunktiot

JavaScript-sovelluksen ([src/main/webapp/js/app.js](src/main/webapp/js/app.js)) lähdekoodissa esiintyy JavaScript-maailmassa yleistyviä nuolifunktioita, esim: `(a, b) => a + b`. Tämä nuolifunktio olisi  lyhyempi syntaksi perinteisille anonyymeille funktioille:

```javascript
function(a, b) {
    return a + b;
}
```

Nuolifunktioiden ja tavallisten anonyymien funktioiden syntaksin lisäksi myös niiden toiminnassa on eroja:

>The handling of this is also different in arrow functions compared to regular functions.
>
>In short, with arrow functions there are no binding of this.
>
>In regular functions the this keyword represented the object that called the function, which could be the window, the document, a button or whatever.
>
>With arrow functions the this keyword always represents the object that defined the arrow function.
>
> *[W3Schools](https://www.w3schools.com/js/js_arrow_function.asp)*

Käytännössä edellä esitetty W3Schools:in selitys tarkoittaa sitä, että kahden seuraavan `onclick`-tapahtumakuuntelijan asettamisessa on myös muitakin eroja kuin ulkonäkö:

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

Perinteiseen tapaan (ylempi) toteutetussa tapahtumankuuntelijassa esiintyvä `this` ei funktiota suoritettaessa viittaakaan enää siihen olioon, jonka sisällä se on määritetty, vaan esimerkiksi siihen painikkeeseen jota klikattiin.

Alemmassa nuolifunktiolla toteutetussa versiossa 
`this`-muuttuja viittaa siihen olioon, jonka metodissa tapahtumakuuntelija asetettiin. Näin funktio on toteutettu myös [tämän projektin lähdekoodissa](src/main/webapp/js/app.js).


## Ajax

Termi "Ajax" on lyhenne sanoista "Asynchronous JavaScript and XML". Nykyään tiedostonsiirtoon käytetään XML-formaatin sijaan JSON-formaattia, mikä teknisestä näkökulmasta ei ole valtava muutos. 

Ajax-teknologioita hyödynnetään tässä projektissa ostoslistan sisällön päivittämisessä dynaamisesti siten, että selain hakee taustalla dataa palvelimelta ja näyttää sen ilman erillistä sivulatausta. Vastaavasti tietojen lisääminen ja poistaminen eivät edellytä sivulatausta, vaan tieto siirtyy taustalla ja päivittyy sivulle dynaamisesti.

### Mikä asynkronisuus?

Ajax-teknologioiden asynkronisuus johtuu siitä, että JavaScript suoritetaan vain yhdessä säikeessä, jossa suoritetaan kerrallaan vain yhtä lauseketta. Jos esimerkiksi tiedonsiirto tehtäisiin synkronisesti, jumittuisi koko JavaScript-sovellus siksi aikaa kunnes tiedonsiirto valmistuu. Voit lukea lisää asynkronisesta ohjelmoinnista esimerkiksi artikkelista ["Understanding Asynchronous JavaScript"](https://blog.bitsrc.io/understanding-asynchronous-javascript-the-event-loop-74cd408419ff) ja Mozillan ["Asynchronous JavaScript" -oppimateriaalista](https://developer.mozilla.org/en-US/docs/Learn/JavaScript/Asynchronous).

### Callback, Promise ja Async/Await
Hitaiden kutsujen ongelma JavaScript-maailmassa antamalla "hitaalle koodille" funktio, joka sen tulee suorittaa operaation valmistuttua. Tällaisia funktioita kutsutaan ns. callback-funktioiksi. Kun koodissa on tarpeen tehdä lukuisia perkkäisiä hitaita operaatioita, syntyy helposti syviä sisäkkäisiä rakenteita, joissa callback-funktiot kutsuvat uusia hitaita operaatioita ja antavat jälleen parametreina uusia callback-funktioita:

```javascript
doSomething(function(result) {
  doSomethingElse(result, function(newResult) {
    doThirdThing(newResult, function(finalResult) {
      console.log('Got the final result: ' + finalResult);
    }, failureCallback);
  }, failureCallback);
}, failureCallback);
```
Lähde: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Using_promises

Ajan saatossa asynkronisten funktioiden ohjelmointityyliksi onkin vakiintunut ns. Promise-oliot, joiden avulla useita asynkronisia kutsuja saadaan kätevästi ketjutettua:

```javascript
doSomething()
.then(result => doSomethingElse(result))
.then(newResult => doThirdThing(newResult))
.then(finalResult => {
  console.log(`Got the final result: ${finalResult}`);
})
.catch(failureCallback);
```
Lähde: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Using_promises

### Poikkeusten käsitteleminen asynkronisessa koodissa

JavaScript 
[Callbacks, Promises and Async/Await](https://medium.com/front-end-weekly/callbacks-promises-and-async-await-ad4756e01d90)

### JSON (JavaScript Object Notation)

Tiedonsiirtomuotona selaimessa toimivan JavaScript-koodin ja palvelimella toimivan Java-koodin välillä on JSON. 

> JSON is a text format that is completely language independent but uses conventions that are familiar to programmers of the C-family of languages, including C, C++, C#, Java, JavaScript, Perl, Python, and many others. These properties make JSON an ideal data-interchange language.
> 
> *https://www.json.org/json-en.html*

Kun sovelluksen pääsivu `index.html` avataan, lataa JS-sovellus ostoslistan senhetkisen sisällön osoitteesta `/api/shoppingList/items`. Vastauksena selain vastaanottaa JSON-dokumentin, jonka sisältö on muodoltaan seuraavanlainen:

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

Vastaus koostuu siis taulukosta `[]`, jonka sisällä on tässä esimerkissä kolme oliota (`{}`). Kullakin oliolla on kaksi attribuuttia: `id` ja `title`, jotka ovat tyypeiltään numero ja merkkijono. Nämä vastaavat suoraan palvelinpäässä määritellyn `ShoppingListItem`-luokan oliomuuttujia:

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
[`src/main/java/model/ShoppingListItem.java`](src/main/java/model/ShoppingListItem.java)

JSON-tiedostomuoto sopii erinomaisesti eri ohjelmointikielien väliseen tiedonvälitykseen ja eri kielillä toteutetut oliot on muutettavissa toisen kielen olioksi parhaimmillaan automaattisesti. JSON-muunnoksia varten tässä esimerkkiprojektissa hydynnetään Googlen kehittämää [Gson-kirjastoa](https://github.com/google/gson). Gson-kirjasto ei ole osa Javan standardikirjastoa, vaan se on lisätty projektiin Maven-työkalun avulla määrittelemällä se [pom.xml](pom.xml)-tiedostoon.

Edellä esitetty JSON-muotoinen esitys ostoslistan sisällöstä generoidaan palvelimella `ShoppingListRestServlet`-luokan `doGet`-metodissa seuraavasti:

```java
List<ShoppingListItem> allItems = dao.getAllItems();

// convert the Java objects into a JSON formatted String:
String json = new Gson().toJson(allItems);
```
[`src/main/java/servlet/ShoppingListRestServlet.java`](src/main/java/servlet/ShoppingListRestServlet.java)

JSON-oliot ovat valmiiksi JavaScript-kielen tukemassa muodossa, joten muunnosta ei JS-koodissa tarvitse erikseen tehdä. `fetch`-kutsun palauttama Http  response -olio antaa JSON:ia vastaavan JavaScript-olion (promiseen käärittynä) kun kutsumme sen `json()`-metodia:

```javascript
try {
    let response = await fetch("/api/shoppingList/items");
    this._items = await response.json();
    this._render();
} catch (error) {
    console.error(error)
    alert('An error occured. Please check the consoles of the browser and the backend.')
}
```
*[src/main/webapp/js/app.js](src/main/webapp/js/app.js)*

### XmlHttpRequest ja tapahtumankuuntelijat

```javascript
// pyyntöolion luonti
var req = new XMLHttpRequest();

// mitä tehdään kun saadaan vastaus (vastauksia voi olla useita)
req.onreadystatechange = function() {
    // jos tila ei ole valmis, ei käsitellä
    if (req.readyState !== this.DONE) {
        console.log("state " + req.readyState);
        return false;
    }

    // jos statuskoodi ei ole 200 (ok), ei käsitellä
    if (req.status !== 200) {
        console.log("status " + req.status);
        return false;
    }

    // näytetään vastaus
    console.log(req.responseText);
}

req.open("GET", "data.json", true);
req.send();
```
Koodiesimerkki: https://web-selainohjelmointi.github.io/#7-Keskustelu-palvelimen-kanssa, CC BY-NC-SA

### jQuery

```javascript
$.getJSON("http://api.icndb.com/jokes/random/5",
    function(data) {
        $.each(data.value, function(i, item) {
            console.log(i);
            console.log(item);
            console.log("-----");
        });
    }
);
```
Koodiesimerkki: http://web-selainohjelmointi.github.io/#10.1-jQuery, CC BY-NC-SA

### Fetch ja promiset
```javascript
fetch('https://jsonplaceholder.typicode.com/todos/1')
  .then(response => response.json())
  .then(json => console.log(json))
```
Koodiesimerkki: https://jsonplaceholder.typicode.com/

### Async/await

## Tämän materiaalin ohjelmointityyli ja teknologiat

Tämä esimerkkiprojekti on toteutettu hyödyntäen JavaScriptin omaa `fetch`-funktiota sekä `async/await`-ohjelmointityyliä. Erillisiä kirjastoja välttämällä opit kirjoittamaan koodiasi yleisemmällä tasolla ja voit hyödyntää taitojasi myös muissa suoritusympäristöissä kuin nettiselaimessa.

Aivan viimeisimpien teknologioiden hyödyntämiseen verkkosovelluksissa liittyy varjopuolia, kuten vaihteleva yhteensopivuus eri selainten ja erityisesti niiden vanhempien versioiden kanssa. Kirjoitushetkellä tässä materiaalissa hyödynnetyt selainteknologiat ovat tuettuja kaikilla moderneilla selaimilla (ks: [nuolifunktiot](https://caniuse.com/#feat=arrow-functions), [fetch](https://caniuse.com/#feat=fetch), [async/await](https://caniuse.com/#feat=async-functions), [template](https://caniuse.com/#feat=template)). 

Lukiessasi tätä materiaalia tänään, voit olla helpottunut siitä, että vanhentuneiden selainversioiden käyttäjämäärät ehtivät vielä jonkin aikaa laskea ennen kuin kirjoitat tuotantokoodia suurelle yleisölle. Monissa tapauksissa nykyaikaisesti kirjoitettu koodi on myös automaattisesti käännettävissä vanhempien selainversioiden ymmärtämään muotoon esimerkiksi [Babel-kääntäjällä](https://babeljs.io/).


## Fetch

https://web-selainohjelmointi.github.io/#4-JavaScript

CodePen?

## Datan näyttäminen sivulla

### `<template>`-tagi
https://developer.mozilla.org/en-US/docs/Web/HTML/Element/template

### Moustache, Pug, jQuery


CodePen?

## `querySelector`

https://developer.mozilla.org/en-US/docs/Web/API/Document/querySelector


## Osallistu tämän materiaalin kehittämiseen

Tämä dokumentaatio on kirjoitettu [markdown](https://guides.github.com/features/mastering-markdown/)-syntaksilla ja sitä ylläpidetään ohjelmistokehittäjien parissa erittäin suositussa GitHub-palvelussa. Voit [esittää kysymyksiä ja kehitysideoita]() sekä tehdä [muutosehdotuksia materiaaliin sekä palvelun lähdekoodeihin]() GitHubissa.

## Lisenssi

Tämä oppimateriaali on lisensoitu [Creative Commons BY-NC-SA 3.0](https://creativecommons.org/licenses/by-nc-sa/3.0/) -lisenssillä. 
