import mongoose from 'mongoose';

const { Schema, model } = mongoose;

const roleSchema = new Schema({
    name: {
        type: String,
        required: [true, 'Name is mandatory'],
        unique: true
    }
});

export const Role = model('Role', roleSchema);

export function toResponse(document) {

    if (document instanceof Array) {
        return document.map(elem => toResponse(elem));
    } else {
        let response = document.toObject({ versionKey: false });
        response.id = response._id.toString();
        delete response._id;
        return response;
    }
}