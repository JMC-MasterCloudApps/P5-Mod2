import mongoose from 'mongoose';

import { commentSchema, toResponse as _toResponse } from './comment.js';

const { Schema, model } = mongoose;

const bookSchema = new Schema({
    title: {
        type: String,
        required: [true, 'Title is mandatory']
    },
    summary: String,
    author: {
        type: String,
        required: [true, 'Author is mandatory']
    },
    publisher: {
        type: String,
        required: [true, 'Publisher is mandatory']
    },
    publicationYear: {
        type: Number,
        validate: {
            validator: function (publicationYear) {
                return Number.isInteger(publicationYear) && publicationYear > 0;
            },
            message: props => `${props.value} is not a valid publication year`
        },
        required: [true, 'Publication year is mandatory']
    },
    comments: [commentSchema]
});

export const Book = model('Book', bookSchema);

export function toResponse(document) {

    if (document instanceof Array) {
        return document.map(elem => JSON.parse('{"id": "' + elem._id.toString() + '","title": "' + elem.title.toString() + '"}'));
    } else {
        let response = document.toObject({ versionKey: false });
        response.id = response._id.toString();
        response.comments = _toResponse(document.comments)
        delete response._id;
        return response;
    }
}
