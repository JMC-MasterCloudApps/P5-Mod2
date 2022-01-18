import express, { json } from 'express';
import { connect, disconnect } from './database.js';
import booksRouter from './routes/bookRouter.js';
import usersRouter from './routes/userRouter.js';
import authRouter from './routes/authRouter.js';

const app = express();

//Convert json bodies to JavaScript object
app.use(json());
app.use('/api/v1/books', booksRouter);
app.use('/api/v1/users', usersRouter);
app.use('/api/v1/auth', authRouter)

async function main() {

    await connect();

    app.listen(3000, () => {
        console.log('Server listening on port 3000!');
    });

    process.on('SIGINT', () => {
        disconnect();
        console.log('Process terminated');
        process.exit(0);
    });
}

main();