import { Router } from 'express';
import { User, toResponse as ResponseUser } from '../models/user.js';
import { Role } from '../models/role.js';
import bcryptjs from 'bcryptjs';

const { hashSync, compareSync } = bcryptjs;

const NOT_FOUND_CODE = 400;

const USERNAME_REQUIRED_RESPONSE = { "error": "User name is mandatory." }
const EMAIL_REQUIRED_RESPONSE = { "error": "Email is mandatory." }
const PASS_REQUIRED_RESPONSE = { "error": "Password is mandatory." }
const ROLE_REQUIRED_RESPONSE = { "error": "Role not found." }
const USERNAME_TAKEN_RESPONSE = { message: "Failed! Username is already in use!" };
const EMAIL_TAKEN_RESPONSE = { message: "Failed! Email is already in use!" };

const router = Router();

router.post('/signup',async (req, res) => {

    let verifyResult = verifyRequest(req);
    if (verifyResult) {
        return res.status(NOT_FOUND_CODE).send(verifyResult);
    }

    let userDB = await User.findOne({nick: req.body.username});
    if (userDB) {
        return res.status(NOT_FOUND_CODE).send(USERNAME_TAKEN_RESPONSE);
    }

    userDB = await User.findOne({email: req.body.email});
    if (userDB) {
        return res.status(NOT_FOUND_CODE).send(EMAIL_TAKEN_RESPONSE);
    }

    let roleDB = await Role.findOne({name: req.body.roles});
    if (!roleDB) {
        console.log(`${ROLE_REQUIRED_RESPONSE.error} Assigning default role: 'user'`);
        roleDB = await Role.findOne({name: "user"});
    }

    const user = new User({
            "nick": req.body.username,
            "email": req.body.email,
            "password": hashSync(req.body.password, 10),
            "roles": roleDB.id
        });

    await user.save()
        .then(userDB => res.json(ResponseUser(userDB)))
        .catch(error => {
            console.log(error);
            res.status(NOT_FOUND_CODE).send(error);
        });
});

function verifyRequest(req) {

    if (!req.body.username) {
        console.log(USERNAME_REQUIRED_RESPONSE);
        return USERNAME_REQUIRED_RESPONSE;
    }

    if (!req.body.email) {
        console.log(EMAIL_REQUIRED_RESPONSE);
        return EMAIL_REQUIRED_RESPONSE;
    }

    if (!req.body.password) {
        console.log(PASS_REQUIRED_RESPONSE);
        return PASS_REQUIRED_RESPONSE;
    }
}

export default router;