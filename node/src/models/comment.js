import mongoose from 'mongoose';
import { toResponse as _toResponse } from './user.js';

const { Schema } = mongoose;

export const commentSchema = new Schema({
    comment: {
        type: String,
        required: [true, 'Comment is mandatory']
    },
    score: {
        type: Number,
        min: [0, 'Score must be at least 0'],
        max: [5, 'Score must be less or equals than 5'],
        required: [true, 'Score is mandatory']
    },
    //Other alternative is just put here the nick and mail for the user to avoid lookup or populate when getting the comment. But when a change is performed in user, these values have to be updated in every comment of the database
    user: {
        type: Schema.Types.ObjectId,
        ref: 'User',
        required: [true, 'User is mandatory']
    }
});

export function toResponse(document) {

    if (document instanceof Array) {
        return document.map(elem => toResponse(elem));

    } else {
        let response = document.toObject({ versionKey: false });
        response.id = response._id.toString();
        delete response._id;
        response.user = _toResponse(document.user)
        delete response.user.id
        return response;
    }
}
