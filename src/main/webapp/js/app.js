class ShoppingListApp {
    constructor(container, itemTemplate, form) {
        this.container = container;
        this.itemTemplate = itemTemplate;
        this.initForm(form);
        
        this.restUrl = '/api/shoppingList/items';
        this.items = [];
    }

    async load() {
        try {
            let response = await fetch(this.restUrl);
            this.items = await response.json();
            this.render();
        } catch (error) {
            console.error(error);
            alert('An error occured. Please check the consoles of the browser and the backend.');
        }
    }

    render() {
        this.container.innerHTML = ''; // Clear contents of the container
        for (let item of this.items) {
            let itemNode = this.renderItem(item);
            this.container.appendChild(itemNode);
        }
    }

    renderItem(item) {
        // Make a copy of the template (true to make a deep copy)
        let newNode = document.importNode(this.itemTemplate.content, true);
        let removeButton = newNode.querySelector('.remove');

        newNode.querySelector('.title').innerText = item.title;

        removeButton.onclick = () => {
            this.deleteItem(item);
        };

        return newNode;
    }

    async storeItem(newItem) {
        try {
            let response = await fetch(this.restUrl, {
                method: 'POST',
                body: JSON.stringify(newItem),
                headers: {
                    'Content-type': 'application/json; charset=UTF-8'
                }
            });
            let json = await response.json();
            this.items.push(json);
            this.render();
            return true;
        } catch (error) {
            console.error(error);
            alert('An error occured. Please check the consoles of the browser and the backend.');
            return false;
        }
    }

    async deleteItem(deleted) {
        try {
            let response = await fetch(this.restUrl + `?id=${deleted.id}`, { method: 'DELETE' });
            this.items = this.items.filter(item => item !== deleted);
            this.render();
        } catch (error) {
            console.error(error);
            alert('An error occured. Please check the consoles of the browser and the backend.');
        }
    }

    initForm(form) {
        form.onsubmit = () => {
            let input = form.querySelector('input');
            let newItem = {
                title: input.value
            };
            this.storeItem(newItem);

            input.value = ''; // clear contents of input field after saving
            return false; // prevent reloading the page
        }
    }
}