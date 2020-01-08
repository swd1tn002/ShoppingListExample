class ShoppingListApp {
    REST_URL = "/api/shoppingList/items";
    _items = [];

    constructor(container, itemTemplate, form) {
        console.log("initializing");
        this._container = container;
        this._itemTemplate = itemTemplate;
        this._initForm(form);
    }

    async load() {
        try {
            let response = await fetch(this.REST_URL);
            this._items = await response.json();
            this._render();
        } catch (error) {
            console.error(error)
            alert('An error occured. Please check the consoles of the browser and the backend.')
        }
    }

    _render() {
        this._container.innerHTML = ""; // Clear contents of the container
        for (let item of this._items) {
            let itemNode = this._renderItem(item);
            this._container.appendChild(itemNode);
        }
    }

    _renderItem(item) {
        // Make a copy of the template (true to make a deep copy)
        let newNode = document.importNode(this._itemTemplate.content, true);
        let removeButton = newNode.querySelector(".remove");

        newNode.querySelector(".title").innerText = item.title;

        removeButton.onclick = () => {
            this.deleteItem(item);
        };

        return newNode;
    }

    async storeItem(newItem) {
        try {
            let response = await fetch(this.REST_URL, {
                method: 'POST',
                body: JSON.stringify(newItem),
                headers: {
                    "Content-type": "application/json; charset=UTF-8"
                }
            });
            let json = await response.json();
            this._items.push(json);
            this._render();
            return true;
        } catch (error) {
            console.error(error)
            alert('An error occured. Please check the consoles of the browser and the backend.')
            return false;
        }
    }

    async deleteItem(deleted) {
        try {
            let response = await fetch(this.REST_URL + `?id=${deleted.id}`, { method: 'DELETE' });
            this._items = this._items.filter(item => item !== deleted);
            this._render();
        } catch (error) {
            console.error(error)
            alert('An error occured. Please check the consoles of the browser and the backend.')
        }
    }

    _initForm(form) {
        form.onsubmit = () => {
            let input = form.querySelector("input");
            let newItem = {
                title: input.value
            };
            this.storeItem(newItem);

            input.value = ""; // clear contents of input field after saving
            return false; // prevent reloading the page
        }
    }
}