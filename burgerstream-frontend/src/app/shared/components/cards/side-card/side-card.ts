import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { Side } from '../../../models/menu/side.model';
import { SizeOption } from '../../../models/menu/sizeOption.model';
import { SideService } from '../../../services/side-service/side-service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'side-card',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './side-card.html',
  styleUrl: './side-card.css'
})
export class SideCard implements OnInit, OnChanges {
    @Input() side!: Side;
    @Input() isAdmin: boolean = true;

    @Output() sideDeleted = new EventEmitter<number>();

    selectedSize?: SizeOption;
    imageUrl = "";

    constructor(private sideService: SideService, private router: Router, private route: ActivatedRoute) { }

    ngOnInit(): void {
        this.selectedSize = this.side.sizeOptions[0];
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes['side'] && this.side?.imageUrl) {
            this.imageUrl = "http://localhost:8080/api/v1/BurgerStream/images/" + this.side.imageUrl;
        }
    }

    updateSide(id?:number){
        if(!id) return;
        this.router.navigate(['update-side', id], { relativeTo: this.route })
    }

    deleteSide() {
        if (this.side.id) {
            if (confirm("Are you sure you wish to delete side: " + this.side.name + "?")) {
                this.sideService.deleteSide(this.side.id).subscribe(
                    data => {
                        this.sideDeleted.emit(this.side.id);
                    }
                )
            }
        } else {
            console.log("I have no idea what this id is");
        }
    }
}
