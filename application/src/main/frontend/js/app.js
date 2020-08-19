import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Link from '@material-ui/core/Link';
import Typography from '@material-ui/core/Typography';
import ShoppingBasket from './shoppingbasket'
import Paper from "@material-ui/core/Paper";

console.clear();

export const useStyles = makeStyles((theme) => ({

    layout: {
        width: 'auto',
        marginLeft: theme.spacing(2),
        marginRight: theme.spacing(2),
        [theme.breakpoints.up(600 + theme.spacing(2) * 2)]: {
            width: 600,
            marginLeft: 'auto',
            marginRight: 'auto',
        },
    },
    paper: {
        marginTop: theme.spacing(3),
        marginBottom: theme.spacing(3),
        padding: theme.spacing(2),
        [theme.breakpoints.up(600 + theme.spacing(3) * 2)]: {
            marginTop: theme.spacing(6),
            marginBottom: theme.spacing(6),
            padding: theme.spacing(3),
        },
    },
    buttons: {
        display: 'flex',
        justifyContent: 'flex-end',
    },
    button: {
        marginTop: theme.spacing(3),
        marginLeft: theme.spacing(1),
    },
}));

function Copyright() {
    return (
        <Typography variant="body2" color="textSecondary" align="center">
            {'Copyright © '}
            <Link href="https://example.com">
                My Company
            </Link>{' '}
            {new Date().getFullYear()}
            {'.'}
        </Typography>
    );
}

function LoginButton() {
    return (
        <button>
            <a href="/login/vipps">Log inn med Vipps</a>
        </button>
    )
}

export default function App() {
    const classes = useStyles();

    return (
        <React.Fragment>
            <CssBaseline />
            <AppBar color={"primary"} style={{position: 'relative'}}>
                <Toolbar>
                    <Typography variant="h6" color="inherit" noWrap>
                        My Service
                    </Typography>
                </Toolbar>
            </AppBar>
            <main className={classes.layout}>
                <Paper className={classes.paper}>
                    <ShoppingBasket userId={"123456"} msisdn={""}/>
                </Paper>
                <Copyright />
                <LoginButton />
            </main>
        </React.Fragment>
    );
}
