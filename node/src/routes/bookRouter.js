import { Router } from 'express';
import { Book, toResponse as toResponseBook } from '../models/book.js';
import { User } from '../models/user.js';
import { toResponse as toResponseComment } from '../models/comment.js';
import mongoose from 'mongoose';
import authJwt from "../auth.js";

const INVALID_BOOK_ID_RESPONSE = { "error": "Invalid book id" };
const BOOK_NOT_FOUND_RESPONSE = { "error": "Book not found" }

const router = Router();

const getAllBooks = async function(req, res) {
    const allBooks = await Book.find().exec();
    res.json(toResponseBook(allBooks));
}

router.get('/', [authJwt.verifyToken, getAllBooks]);

//	b. Obtener toda la información de un libro determinado y además sus
//	comentarios.
router.get('/:id', async (req, res) => {
    const id = req.params.id;

    if (!mongoose.Types.ObjectId.isValid(id)) {
        return res.status(400).send(INVALID_BOOK_ID_RESPONSE);
    }

    //If nick and name is placed in the comment, there is no need to populate.
    const book = await Book.findById(id).populate('comments.user');
    
    if (!book) {
        return res.status(404).send(BOOK_NOT_FOUND_RESPONSE);
    }

    res.json(toResponseBook(book));
});

//	e. Obtener la información de un comentario concreto.
router.get('/:id/comments/:commentId', async (req, res) => {
    const id = req.params.id;
    const commentId = req.params.commentId;

    if (!mongoose.Types.ObjectId.isValid(id)) {
        return res.status(400).send(INVALID_BOOK_ID_RESPONSE);
    }
    if (!mongoose.Types.ObjectId.isValid(commentId)) {
        return res.status(400).send({ "error": "Invalid comment id" });
    }

    const book = await Book.findById(id).populate('comments.user');
    if (!book) {
        return res.status(404).send(BOOK_NOT_FOUND_RESPONSE);
    }

    const comment = await book.comments.id(commentId);
    if (!comment) {
        return res.status(404).send({ "error": "Comment not found" });
    }

    res.json(toResponseComment(comment));
});

//	c. Crear un libro.
router.post('/', async (req, res) => {

    const book = new Book({
        title: req.body.title,
        summary: req.body.summary,
        author: req.body.author,
        publisher: req.body.publisher,
        publicationYear: req.body.publicationYear,
    });

    await book.save()
        .then(savedBook => res.json(toResponseBook(savedBook)))
        .catch(error => {
            console.log(error);
            res.status(400).send(error);
        });

});

//	d. Crear un comentario asociado a un libro.
router.post('/:id/comments', async (req, res) => {
    const id = req.params.id;

    if (!mongoose.Types.ObjectId.isValid(id)) {
        return res.status(400).send(INVALID_BOOK_ID_RESPONSE);
    }
    if (!req.body.userNick) {
        return res.status(400).send({ "error": "User nick is mandatory" });
    }

    const book = await Book.findById(id);
    if (!book) {
        return res.status(404).send(BOOK_NOT_FOUND_RESPONSE);
    }

    const user = await User.findOne({ nick: req.body.userNick })
    if (!user) {
        return res.status(404).json({ "error": "User not found" });
    };

    book.comments.push({
        comment: req.body.comment,
        score: req.body.score,
        user: user._id
    });

    await book.save()
        .then(async savedBook => {
            await savedBook.populate(['comments', savedBook.comments.length - 1, 'user'].join('.'));
            const comment = savedBook.comments[savedBook.comments.length - 1];
            res.json(toResponseComment(comment));
        })
        .catch(error => {
            console.log(error);
            res.status(400).send(error);
        });

});

//	b. Borrar un libro
router.delete('/:id', async (req, res) => {
    const id = req.params.id;

    if (!mongoose.Types.ObjectId.isValid(id)) {
        return res.status(400).send(INVALID_BOOK_ID_RESPONSE);
    }

    const book = await Book.findById(id).populate('comments.user');
    if (!book) {
        return res.status(404).send(BOOK_NOT_FOUND_RESPONSE);
    }

    await book.delete();
    res.json(toResponseBook(book));

});

//	c. Borrar un comentario
router.delete('/:id/comments/:commentId', async (req, res) => {
    const id = req.params.id;
    const commentId = req.params.commentId;

    if (!mongoose.Types.ObjectId.isValid(id)) {
        return res.status(400).send(INVALID_BOOK_ID_RESPONSE);
    }
    if (!mongoose.Types.ObjectId.isValid(commentId)) {
        return res.status(400).send({ "error": "Invalid comment id" });
    }

    const book = await Book.findById(id).populate('comments.user');
    if (!book) {
        return res.status(404).send(BOOK_NOT_FOUND_RESPONSE);
    }

    const comment = await book.comments.id(commentId);
    if (!comment) {
        return res.status(404).send({ "error": "Comment not found" });
    }

    comment.remove();
    await book.save();
    res.json(toResponseComment(comment));

});

export default router;