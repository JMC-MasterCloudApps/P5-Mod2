import express, { json } from 'express';
import { readFileSync } from 'fs';
import { createServer } from 'https';
import { connect, disconnect } from './database.js';
import booksRouter from './routes/bookRouter.js';
import usersRouter from './routes/userRouter.js';
import authRouter from './routes/authRouter.js';

const httpsOptions = {
    key: readFileSync('server.key'),
    cert: readFileSync('server.cert')};

const SERVER_PORT = 3000;
const app = express();

//Convert json bodies to JavaScript object
app.use(json());
app.use('/api/v1/books', booksRouter);
app.use('/api/v1/users', usersRouter);
app.use('/api/v1/auth', authRouter);
app.use(function(req, res, next){
    res.header(
        'Access-Control-Allow-Headers',
        'x-access-token, Origin, Content-Type, Accept'
    );
    next();
});

async function main() {

    await connect();

    createServer(httpsOptions, app)
        .listen(SERVER_PORT, () => console.log('Server listening on port 3000!'));

    process.on('SIGINT', () => {
        disconnect();
        console.log('Process terminated');
        process.exit(0);
    });
}

main();