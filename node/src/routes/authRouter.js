import { Router } from 'express';
import { User, toResponse as ResponseUser } from '../models/user.js';
import { Role } from '../models/role.js';
import { toResponse as toResponseComment } from '../models/comment.js';
import mongoose from 'mongoose';

const NOT_FOUND_CODE = 400;

const USERNAME_REQUIRED_RESPONSE = { "error": "User name is mandatory." }
const EMAIL_REQUIRED_RESPONSE = { "error": "Email is mandatory." }
const PASS_REQUIRED_RESPONSE = { "error": "Password is mandatory." }
const ROLE_REQUIRED_RESPONSE = { "error": "Role not found." }

const router = Router();

router.post('/signup', async (req, res) => {

    if (!req.body.username) {
        console.log(USERNAME_REQUIRED_RESPONSE);
        return res.status(NOT_FOUND_CODE).send(USERNAME_REQUIRED_RESPONSE);
    }

    if (!req.body.email) {
        console.log(EMAIL_REQUIRED_RESPONSE);
        return res.status(NOT_FOUND_CODE).send(EMAIL_REQUIRED_RESPONSE);
    }

    if (!req.body.password) {
        console.log(PASS_REQUIRED_RESPONSE);
        return res.status(NOT_FOUND_CODE).send(PASS_REQUIRED_RESPONSE);
    }

    const roleDB = await Role.findOne({name: req.body.roles});
    if (!roleDB) {
        console.log(ROLE_REQUIRED_RESPONSE);
        return res.status(NOT_FOUND_CODE).send(ROLE_REQUIRED_RESPONSE);
    }

    const user = new User({
            "nick": req.body.username,
            "email": req.body.email,
            "password": req.body.password,
            "roles": roleDB.id
        });

    await user.save()
        .then(userDB => res.json(ResponseUser(userDB)))
        .catch(error => {
            console.log(error);
            res.status(400).send(error);
        });

});


export default router;