export class SizeOption {
    id: number;
    label: string;
    sizeLabel: string;
    extraPrice: number;

    constructor(id: number, label: string, sizeLabel: string, extraPrice: number){
        this.id = id;
        this.label = label;
        this.sizeLabel = sizeLabel;
        this.extraPrice = extraPrice;
    }
}