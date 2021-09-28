export class User {
    constructor() { };

    // private _id!: string;
    private _email!: string;
    private _firstName!: string;
    private _lastName!: string;
    private _password!: string;
    private _confirmPassword!: string;
}

export class UserInfo {

    constructor() { };

    private _firstName!: string;
    private _lastName!: string;
    private _id!: string;
    private _email!: string;

    get id() {
        return this._id;
    }
}