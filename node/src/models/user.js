import mongoose from 'mongoose';

const { Schema, model } = mongoose;

export const isValidEmail = function(email) {
    let mailformat = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

    return typeof email == 'string' && email != "" && mailformat.test(email);
}

const userSchema = new Schema({
    nick: {
        type: String,
        required: [true, 'Nick is mandatory'],
        unique: true
    },
    email: {
        type: String,
        validate: {
            validator: isValidEmail,
            message: props => `${props.value} is not a valid email`
        },
        required: [true, 'Email is mandatory']
    },
    password: {
        type: String,
        required: [true, 'Password is mandatory']
    },
    roles: [{
        type: Schema.Types.ObjectId,
        ref: 'Role'
    }]
});

export const User = model('User', userSchema);

export function toResponse(document) {

    if (document instanceof Array) {
        return document.map(elem => toResponse(elem));
    } else {
        let response = document.toObject({ versionKey: false });
        delete response._id;
        delete response.password;
        delete response.roles;
        return response;
    }
}