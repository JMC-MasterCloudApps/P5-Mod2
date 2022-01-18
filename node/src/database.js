import mongoose from 'mongoose';
import { Role } from './models/role.js';
import { User } from './models/user.js';
import { Book } from './models/book.js';

const url = 'mongodb://localhost:27017/booksDB';
const ADMIN = {name: 'admin'}
const USER = {name: 'user'}
const USER1 = {
    _id: new mongoose.Types.ObjectId('5fda3234e9e3fd53e3907bed'),
    nick: 'user1',
    email: 'user1@email.es',
    password: 'pass'
};
const USER2 = {
    _id: new mongoose.Types.ObjectId('5fda3234e9e3fd53e3907bef'),
    nick: 'user2',
    email: 'user2@email.es',
    password: 'pass'
};
const BOOK1 = {
    _id: new mongoose.Types.ObjectId('5fda3234e9e3fd53e3907bf0'),
    title: 'Book 1 title',
    summary: 'Book 1 summary',
    author: 'Book 1 author',
    publisher: 'Book 1 publisher',
    publicationYear: 1992
};
const BOOK2 = {
    _id: new mongoose.Types.ObjectId('5fda350d3749aa4832165b84'),
    title: 'Book 2 title',
    summary: 'Book 2 summary',
    author: 'Book 2 author',
    publisher: 'Book 2 publisher',
    publicationYear: 2006
};
const COMMENT1 = {
    _id: new mongoose.Types.ObjectId('5fdb4812df5c2555a401b6da'),
    comment: 'Book 2 comment 1 from user 1',
    score: 2.6,
    user: new mongoose.Types.ObjectId('5fda3234e9e3fd53e3907bed')
};
const COMMENT2 = {
    _id: new mongoose.Types.ObjectId('5fdb4812df5c2555a401b6db'),
    comment: 'Book 2 comment 2 from user 1',
    score: 4,
    user: new mongoose.Types.ObjectId('5fda3234e9e3fd53e3907bed')
};

export async function connect() {

    await mongoose.connect(url, {
        useUnifiedTopology: true,
        useNewUrlParser: true,
        // useFindAndModify: false
    })
    .then(() => console.log('Connected to MongoDB'))
    .catch(err => console.log('Connection error', err));

    await init();
}

export async function disconnect() {
    await mongoose.connection.close();
    console.log('Disconnected from MongoDB')
}

async function init() {

    console.log('Initializing database');
    console.log('Populating database with roles')
    await Role.deleteMany({});
    const dbAdmin = await new Role(ADMIN).save();
    const dbuser = await new Role(USER).save();

    console.log('Populating database with users');
    await User.deleteMany({});
    USER1.roles = dbAdmin.id;
    USER2.roles = dbuser.id;
    await new User(USER1).save();
    await new User(USER2).save();

    console.log('Populating database with books');
    await Book.deleteMany({});
    await new Book(BOOK1).save();
    const book2nd = await new Book(BOOK2).save();

    console.log('Populating database with comments');
    book2nd.comments.push(COMMENT1);
    book2nd.comments.push(COMMENT2);
    await book2nd.save();

    console.log('Database initialized');
}