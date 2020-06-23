import React from 'react';
import CircularProgress from "@material-ui/core/CircularProgress";
import {getAllProducts, initPayment} from './repository'
import Button from "@material-ui/core/Button";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import ButtonBase from "@material-ui/core/ButtonBase";
import Typography from "@material-ui/core/Typography";
import PropTypes from "prop-types";
import Link from "@material-ui/core/Link";

/* Product */
class Product extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            qty: 0
        };
        this.add = this.add.bind(this);
        this.subtract = this.subtract.bind(this);
    }

    add() {
        let qty = this.state.qty + 1;

        this.setState({
            qty: qty
        });
        this.props.handleTotal(this.props.price);
        this.props.addOrderItem(this.props.name);
    }

    subtract() {
        let qty = this.state.qty - 1;

        this.setState({
            qty: qty
        });
        this.props.handleTotal(-this.props.price);
        this.props.removeOrderItem(this.props.name);
    }

    render() {
        return (
            <Paper style={{
                padding: 2,
                margin: 3
            }}>
                <Grid container spacing={2}>
                    <Grid item xs>
                        <ButtonBase>
                            <img style={{
                                margin: 'auto',
                                display: 'block',
                                maxWidth: '100%',
                                maxHeight: '100%',
                            }} alt="item" src="/images/image.png" />
                        </ButtonBase>
                    </Grid>
                    <Grid item xs={12} sm container>
                        <Typography gutterBottom variant="subtitle1">
                            {this.props.name}: NOK{this.props.price}
                        </Typography>
                        <Typography variant="body2" gutterBottom>
                            {this.props.description}
                        </Typography>
                    </Grid>
                    <Grid item xs container direction="column" justify={"center"} alignItems={"center"}>
                        <Grid item xs>
                            <Button variant="outlined" color="primary"
                                    onClick={this.subtract} disabled={this.state.qty < 1}>
                                -
                            </Button>
                        </Grid>
                        <Grid item xs={12}>
                            {this.state.qty}
                        </Grid>
                        <Grid item xs>
                            <Button variant="outlined" color="primary"
                                    onClick={this.add}>
                                +
                            </Button>
                        </Grid>
                    </Grid>
                </Grid>
            </Paper>
        );
    }
}

Product.propTypes = {
    name: PropTypes.string.isRequired,
    price: PropTypes.number.isRequired,
    description: PropTypes.string,
    handleTotal: PropTypes.func.isRequired,
    addOrderItem: PropTypes.func.isRequired,
    removeOrderItem: PropTypes.func.isRequired
};

/* Total */
class Total extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let total = this.props.total.toFixed(2);

        return (
            <Paper style={{padding: 4, margin: 'auto'}}>
                <hr />
                <hr />
                <Typography gutterBottom variant={"subtitle1"} align={"right"}>
                    Total: NOK{total}
                </Typography>
                <hr />
            </Paper>
        );
    }
}

Total.propTypes = {
    total: PropTypes.number.isRequired
}

/* ProductList */
class ProductList extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            total: 0,
            productList: [],
            orderList: []
        };

        this.calculateTotal = this.calculateTotal.bind(this);
        this.addOrderItem = this.addOrderItem.bind(this);
        this.removeOrderItem = this.removeOrderItem.bind(this);
    }

    componentDidMount() {
        setTimeout(() => {
            getAllProducts().then(data => { this.setState({ productList: data });});
        }, 1000);
    }

    // Calculates the total price based on input and current total
    calculateTotal(price) {
        this.setState({
            total: this.state.total + price
        });
    }

    // Adds an item to the list of orders
    addOrderItem(name) {
        console.log("Adding " + name + " to order...")

        let orderList = this.state.orderList;

        let idx = orderList.findIndex((item) => {
            return item.name === name;
        })

        if (idx >= 0) {
            orderList[idx].quantity = orderList[idx].quantity + 1; // Update the quantity
        }
        else {
            orderList.push({name: name, quantity: 1}); // Add the item
        }

        this.setState({orderList: orderList});
    }

    // Removes an item from the list of orders
    removeOrderItem(name) {
        console.log("Removing " + name + " from order...")

        let orderList = this.state.orderList;

        let idx = orderList.findIndex((item) => {
            return item.name === name;
        })

        if (idx >= 0) {
            let qty = orderList[idx].quantity - 1;

            if (qty < 1) {
                // Remove the item all together
                orderList.splice(idx, 1);
            }
            else {
                orderList[idx].quantity = qty; // Update the quantity
            }

            this.setState({orderList: orderList});
        }
    }

    render() {
        let component = this;

        let products = this.state.productList.map( (product) => {
            return (
                <Product
                    key={product.name}
                    name={product.name}
                    price={product.price}
                    info={product.details}
                    handleTotal={component.calculateTotal}
                    addOrderItem={component.addOrderItem}
                    removeOrderItem={component.removeOrderItem}
                />
            );
        });

        return (
            <>
                {products}
                <Total total={this.state.total} />
                <Button variant="contained" color="primary"
                        onClick={ () => { this.props.completeOrder(this.state.orderList) } }>
                    {'Place order'}
                </Button>
            </>
        );
    }
}

export default class ShoppingBasket extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            isLoading: false,
            isError: false,
            orderList: [],
            orderId: null,
            url: null
        };

        this.createOrder = this.createOrder.bind(this);
    }

    createOrder(orders) {
        console.log("Creating order for: " + JSON.stringify(orders));
        this.setState({isLoading: true})

        let component = this;

        initPayment(this.props.userId, this.props.msisdn, orders)
            .then( (response) => {
                // Fallback to safe state
                component.setState({isLoading: false, isError: true});

                if (response.orderId && response.url) {
                    component.setState({
                        isError: false,
                        orderId: response.orderId,
                        url: response.url
                    })
                    return;
                }

                console.log("The request failed: " + response.statusText);
            });
    }

    render() {
        if (this.state.isError) {
            return (<>
                <Typography variant="h5" gutterBottom>
                    Something went wrong.
                </Typography>
                <Typography variant="subtitle1">
                    Your order cannot be processed at this time. Please try again later!
                </Typography>
            </>);
        }

        if (this.state.isLoading) {
            return <CircularProgress />
        }

        if (this.state.orderId && this.state.url) {
            return <>
                <Typography variant="h5" gutterBottom>
                    Your order has been placed! Your order ID is {this.state.orderId}.
                </Typography>
                <Typography variant="subtitle1">
                    Click <Link color="inherit" href={this.state.url}>here</Link> to proceed with payment!
                </Typography>
            </>;
        }

        return (
            <div>
                <ProductList orderList={this.state.orderList} completeOrder={this.createOrder} />
            </div>
        );
    }
}

ShoppingBasket.propTypes = {
    userId: PropTypes.string.isRequired,
    msisdn: PropTypes.string.isRequired
}
