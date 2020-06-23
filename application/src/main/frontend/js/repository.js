
export function getAllProducts() {
	return fetch('/product')
	.then(response => response.json())
}

export function getProducts(products) {
	return fetch('/product', products)
		.then(response => response.json())
}

export async function initPayment(user_id, mobile_number, products) {
	const order = {userId: user_id, mobileNumber: mobile_number, products: products}

	console.log("Initializing payment for order: " + JSON.stringify(order));

	const response = await fetch('initiatePayment', {
		method: 'POST',
		mode: 'cors',
		credentials: 'same-origin',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(order)
	})

	return await response.json();
}
